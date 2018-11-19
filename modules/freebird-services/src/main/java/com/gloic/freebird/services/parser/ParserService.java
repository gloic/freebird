package com.gloic.freebird.services.parser;

import com.gloic.freebird.commons.enumerations.HostType;
import com.gloic.freebird.commons.util.JsoupUtil;
import com.gloic.freebird.commons.util.UtilsParser;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.services.parser.hostParser.ApacheParser;
import com.gloic.freebird.services.parser.hostParser.DefaultParser;
import com.gloic.freebird.services.parser.hostParser.IISParser;
import com.gloic.freebird.services.parser.hostParser.IParserMapper;
import com.gloic.freebird.services.parser.hostParser.LighttpdParser;
import com.gloic.freebird.services.scraper.ScraperService;
import com.gloic.freebird.services.scraper.ScraperWatcherService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parser that extract data from each item of a page
 *
 * @author gloic
 */
@Service
@Slf4j
public class ParserService {

    private static final String FOLDERS_MAP_ENTRY = "folders";
    private static final String FILES_MAP_ENTRY = "files";
    private final LinkService linkService;
    private final ScraperService scaperService;

    @Value("${parser.thresholds.invalidRedirection}")
    private int invalidRedirectionThreshold;

    @Value("${scraper.asynchronous}")
    private boolean asynchronousScrape;

    private final ScraperWatcherService scraperWatcherService;

    @Autowired
    public ParserService(LinkService linkService, ScraperService scaperService, ScraperWatcherService scraperWatcherService) {
        this.linkService = linkService;
        this.scaperService = scaperService;
        this.scraperWatcherService = scraperWatcherService;
    }

    /**
     * Entry point of the service. Access to the main page of a site and recursively browse all pages
     *
     * @param site
     * @param redoUnknowns
     * @return
     */
    public Set<String> parse(Site site, boolean redoUnknowns) {
        log.info("Parsing of site:{}", site.getUrl());
        IParserMapper parser = getParser(site.getType());
        log.info("parser type:{}", site.getType());

        // Flag all files as PENDING
        log.info("Flagging all files as PENDING");
        linkService.flagAsPendingBySite(site);

        if (redoUnknowns) {
            linkService.deleteUnknownsBySite(site);
        }

        parsePage(parser, site.getUrl(), site, true, new HashMap<>(), "");

        // Delete all PENDING files
        log.info("Deleting all PENDING files");
        linkService.deletePendingBySite(site);

        return null;
    }

    /**
     * Parse a given page. extract data of files if necessary and going recursively into subfolders
     *
     * @param parser
     * @param urlToParse
     * @param site
     * @param doProcessFiles
     */
    private void parsePage(final IParserMapper parser, final String urlToParse, final Site site, final boolean doProcessFiles, Map<String, List<Element>> previousContent, String previousFolder) {
        final String decoded = UtilsParser.decodeURL(urlToParse);

        log.debug("Parsing of {}, doProcessFiles={}", decoded, doProcessFiles);

        // Extract files and folders of the current page
        Map<String, List<Element>> foldersAndFilesMap = getFoldersAndFiles(parser, urlToParse, site);
        if (previousFolder.equals(urlToParse.substring(urlToParse.lastIndexOf("/", urlToParse.length() - 2), urlToParse.length() - 1))
                && previousContent.get(FOLDERS_MAP_ENTRY).size() == foldersAndFilesMap.get(FOLDERS_MAP_ENTRY).size()
                && previousContent.get(FILES_MAP_ENTRY).size() == foldersAndFilesMap.get(FILES_MAP_ENTRY).size()) {
            log.error("Potential infinite loop detected for folder : '{}' at url: {}", previousFolder, urlToParse);
            invalidRedirectionThreshold++;
            return;
        }

        // nothing to do here if not file nor folder
        if (foldersAndFilesMap == null || (foldersAndFilesMap.get(FOLDERS_MAP_ENTRY).isEmpty() && foldersAndFilesMap.get(FILES_MAP_ENTRY).isEmpty())) {
            log.debug(" -foldersAndFilesMap is null or empty: nor file or folders");
            return;
        }

        log.debug(" -foldersAndFilesMap contains {} folders and {} files", foldersAndFilesMap.get(FOLDERS_MAP_ENTRY).size(), foldersAndFilesMap.get(FILES_MAP_ENTRY).size());

        if (doProcessFiles) {
            // Check if some media files was already found in db
            Collection<Link> mediasInFolder = linkService.findBySiteAndParentUrl(site, decoded);
            log.debug(" -mediasInFolder contains {} items", mediasInFolder.size());

            // for each file in page, check if it was already stored in previous scan
            for (Element fileElement : foldersAndFilesMap.get(FILES_MAP_ENTRY)) {
                if (mediasInFolder.isEmpty()) {
                    // no file in this folder : scrape it
                    ItemToParse itemToParse = parser.getItemToParse(fileElement, site);
                    log.debug(" -no files in DB, scraping media: {}", itemToParse.getFilename());
                    // Scrape now or later
                    if (asynchronousScrape) {
                        scraperWatcherService.addItem(itemToParse);
//                        scraperWatcherService.processItem(itemToParse);
                    } else {
                        scaperService.scrape(itemToParse);
                    }
                } else {
                    Link media = mediasInFolder.stream().filter(m -> m.getUrl().equals(parser.getLink(fileElement).attr("abs:href"))).findFirst().orElse(null);
                    if (media != null) {
                        log.debug(" -media already scraped. Updating its status to ONLINE");
                        // updating link status to online
                        linkService.setOnline(media);
                    } else {
                        // scraping a new file
                        ItemToParse itemToParse = parser.getItemToParse(fileElement, site);
                        log.debug(" -new media, scraping it. Filename: {}, date:{}", itemToParse.getFilename(), itemToParse.getLastModified());
                        // Scrape now or later
                        if (asynchronousScrape) {
                            scraperWatcherService.addItem(itemToParse);
//                            scraperWatcherService.processItem(itemToParse);
                        } else {
                            scaperService.scrape(itemToParse);
                        }
                    }
                }
            }
        } else {
            log.debug(" -folder not updated, marking all files to ONLINE for parentUrl:{}", decoded);
            // Files are ignored because the folder was not updated. All files are set as ONLINE
            linkService.flagAsOnlineBySiteAndParentUrls(site, decoded);
        }

        // Recursively browse sub folders
        for (Element folderElement : foldersAndFilesMap.get(FOLDERS_MAP_ENTRY)) {
            String folderUrl = parser.getLink(folderElement).attr("abs:href");
            if (!site.getIgnoredUrls().contains(folderUrl)) {
                // Also check if the folder was updated since last scan
                previousContent = new HashMap<>(foldersAndFilesMap);
                previousFolder = new String(urlToParse.substring(urlToParse.lastIndexOf("/", urlToParse.length() - 2), urlToParse.length() - 1));
                parsePage(parser, folderUrl, site, doProcessFiles(parser, folderElement, site.getLastScan()), previousContent, previousFolder);
            }
        }
    }

    /**
     * Build and return a map made of "files" and "folders"
     *
     * @param parser
     * @param urlToParse
     * @param site
     * @return
     */
    private Map<String, List<Element>> getFoldersAndFiles(IParserMapper parser, String urlToParse, Site site) {
        Document doc = JsoupUtil.getDocument(urlToParse);
        if (doc == null) return null;

        Map<String, List<Element>> result = new HashMap<>();
        result.put(FOLDERS_MAP_ENTRY, new ArrayList<>());
        result.put(FILES_MAP_ENTRY, new ArrayList<>());

        int invalidRedirection = 0;
        for (Element row : parser.getAllRow(doc)) {
            if (invalidRedirection > invalidRedirectionThreshold) {
                log.error("Too many invalid redirection. Skipping this site.");
                throw new RuntimeException();
            }

            // Extract destination link
            final Element link = parser.getLink(row);
            final String url = link.attr("abs:href");
            final String finalDest = link.attr("href");

            // if the destination is parent directory or a shorter than current
            if (finalDest.equals("/") || url.length() < doc.baseUri().length()) {
                continue;
            } else if (!url.contains(site.getUrl())) {
                // Avoid trap sites with amazon or adf.ly links
                log.warn("The link destination is on another website: Origin:{}, Destination: {}", urlToParse, url);
                log.warn("invalidRedirection counter: {}/{}", invalidRedirection, invalidRedirectionThreshold);
                invalidRedirection++;
                continue;
            }

            if (parser.isDirectory(row)) {
                // It's a folder
                result.get(FOLDERS_MAP_ENTRY).add(row);
            } else if (scaperService.isMedia(finalDest)) {
                result.get(FILES_MAP_ENTRY).add(row);
            }
        }
        return result;
    }

    /**
     * Check if the files need to be processed for the given folder element by comparing its date and the last site scan
     *
     * @param parser
     * @param element
     * @param siteLastScan
     * @return true if the folder or its content was modified since last scan
     */
    private boolean doProcessFiles(IParserMapper parser, Element element, LocalDateTime siteLastScan) {
        LocalDateTime lastModified = parser.getLastModifiedDate(element);
        return lastModified == null || siteLastScan == null || lastModified.isAfter(siteLastScan);
    }

    /**
     * Get parser from host type
     * TODO create a factory instead of new item each time
     *
     * @param hostType
     * @return
     */
    private IParserMapper getParser(HostType hostType) {
        switch (hostType) {
            case APACHE:
                return new ApacheParser();
            case IIS:
                return new IISParser();
            case LIGHTTPD:
                return new LighttpdParser();
            case UNKNOWN:
                return new DefaultParser();
        }
        return null;
    }
}
