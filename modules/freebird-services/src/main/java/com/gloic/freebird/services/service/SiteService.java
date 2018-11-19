package com.gloic.freebird.services.service;

import com.gloic.freebird.commons.enumerations.FileStatus;
import com.gloic.freebird.commons.enumerations.HostType;
import com.gloic.freebird.commons.enumerations.MediaCategory;
import com.gloic.freebird.commons.enumerations.SiteStatus;
import com.gloic.freebird.commons.util.JsoupUtil;
import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.persistence.repository.SiteRepository;
import com.gloic.freebird.services.parser.LinkService;
import com.gloic.freebird.services.parser.ParserService;
import com.gloic.freebird.services.wrapper.SiteWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage Sites (from Administration panel)
 *
 * @author gloic
 */
@Service
@Slf4j
public class SiteService {

    private final SiteRepository siteRepository;
    private final ParserService parserService;
    private final LinkService linkService;

    @Autowired
    public SiteService(SiteRepository siteRepository, ParserService siteExplorerService, LinkService linkService) {
        this.siteRepository = siteRepository;
        this.parserService = siteExplorerService;
        this.linkService = linkService;
    }

    /**
     * Find all sites and adds stats information
     *
     * @return
     */
    public List<SiteWrapper> findAll() {
        List<SiteWrapper> siteVos = new ArrayList<>();
        for (Site site : siteRepository.findAll()) {
            SiteWrapper vo = new SiteWrapper(site);

            vo.setNumberMovieOnline(siteRepository.findBySiteNumberOfLinkByCategoryAndFileStatus(site.getId(), MediaCategory.MOVIE, FileStatus.ONLINE));
            vo.setNumberEpisodeOnline(siteRepository.findBySiteNumberOfLinkByCategoryAndFileStatus(site.getId(), MediaCategory.TV_SHOW, FileStatus.ONLINE));
            vo.setNumberUnknownOnline(siteRepository.findBySiteNumberOfLinkByCategoryAndFileStatus(site.getId(), MediaCategory.UNKNOWN, FileStatus.ONLINE));

            vo.setNumberMovieNew(siteRepository.findBySiteNumberOfLinkByCategoryAndFileStatus(site.getId(), MediaCategory.MOVIE, FileStatus.NEW));
            vo.setNumberEpisodeNew(siteRepository.findBySiteNumberOfLinkByCategoryAndFileStatus(site.getId(), MediaCategory.TV_SHOW, FileStatus.NEW));
            vo.setNumberUnknownNew(siteRepository.findBySiteNumberOfLinkByCategoryAndFileStatus(site.getId(), MediaCategory.UNKNOWN, FileStatus.NEW));
            siteVos.add(vo);
        }
        return siteVos;
    }

    /**
     * Persist a new site
     *
     * @param siteToAdd
     * @return
     */
    public Site addSite(Site siteToAdd) {
        Site newSite = new Site();
        newSite.setUrl(siteToAdd.getUrl());
        newSite.setType(getHost(siteToAdd.getUrl()));
        return siteRepository.save(newSite);
    }

    @Scheduled(cron = "${parser.cron}")
    public void scanAll() {
        scanAll(false);
    }

    public void scanAll(boolean redoUnknowns) {
        log.info("Starting scan of all sites");
        siteRepository.findAll().forEach(s -> scan(s, redoUnknowns));
    }

    public void scan(Long id, boolean redoUnknowns) {
        scan(siteRepository.findOne(id), redoUnknowns);
    }

    /**
     * Start the scan of a site.
     *
     * @param site
     * @param redoUnknowns
     */
    public void scan(Site site, boolean redoUnknowns) {
        log.info("Scanning site: '{}'", site.getUrl());

        // check if the site is reachable
        if (JsoupUtil.getDocument(site.getUrl()) == null) {
            log.error("Site with id={} is OFFLINE: {}", site.getId(), site.getUrl());
            // site.setUrl("http://ns3307805.ip-178-32-220.eu/Films/17%20Again%20(2009)1/");
            // Do not reflag as OFFLINE. In order to known when was the last ONLINE date.
            if (site.getSiteStatus() == null || !site.getSiteStatus().equals(SiteStatus.OFFLINE)) {
                site.setLastScan(LocalDateTime.now());
                site.setSiteStatus(SiteStatus.OFFLINE);
                linkService.deleteAllBySite(site);
                siteRepository.save(site);
            }
            return;
        } else {
            site.setSiteStatus(SiteStatus.SCANNING);
            siteRepository.save(site);
        }

        // TODO check if the site speed is correct

        // Get host type if it's not yet done
        if (site.getType() == null) {
            site.setType(getHost(site.getUrl()));
        }

        log.debug("Exploration of site : {}", site.getUrl());

        // Start site parsing
        parserService.parse(site, redoUnknowns);

        // Update site's last scan date
        site.setLastScan(LocalDateTime.now());
        site.setSiteStatus(SiteStatus.ONLINE);
        siteRepository.save(site);

        log.info("Exploration finished for '{}'", site.getUrl());
    }

    /**
     * Tries to find what is the site's host regarding the columns and the page's content
     *
     * @param url
     * @return
     */
    private HostType getHost(String url) {
        Document document = JsoupUtil.getDocument(url);
        if (document == null) return HostType.UNKNOWN;
        // First : check if the host type is wrote on the current document

        // Apache case
        Elements elementsAddress = document.getElementsByTag("address");

        if (elementsAddress != null && elementsAddress.size() > 0 && elementsAddress.first().text().contains("Apache")) {
            return HostType.APACHE;
        }

        // Lighttpd case
        Elements elementsClassFoot = document.getElementsByClass("foot");
        if (elementsClassFoot != null && elementsClassFoot.size() > 0
                && elementsClassFoot.first().text().contains("lighttpd")) {
            return HostType.LIGHTTPD;
        }

        // IIS case
        if (elementsAddress != null && elementsAddress.size() > 0 && elementsAddress.first().text().contains("IIS")) {
            // TODO IIS IS NOT YET IMPLEMENTED
            return HostType.IIS;
        }

        /*
         * If the host type is not shown, check header to find the host
         */

        Elements rows = document.getElementsByTag("tr");
        if (rows.size() > 0) {
            Elements headers = rows.first().getElementsByTag("th");
            if (headers.size() > 0) {

                // Extract the first line of the table
                String col1 = headers.get(0).text();
                String col2 = headers.get(1).text();
                String col3 = headers.get(2).text();
                String col4 = headers.get(3).text();

                if (headers.size() == 5) {
                    // It can be apache
                    String col5 = headers.get(4).text();

                    // Apache's format (ico) Name Last modified Size Description
                    if (col1.equals("") && col2.equals("Name") && col3.equals("Last modified") && col4.equals("Size")
                            && col5.equals("Description")) {
                        return HostType.APACHE;
                    }
                } else if (headers.size() == 4) {
                    // can be lightttp

                    // Lighttpd's format Name Last Modified Size Type
                    if (col1.equals("Name") && col2.equals("Last Modified") && col3.equals("Size")
                            && col4.equals("Type")) {
                        return HostType.LIGHTTPD;
                    }
                }
            }
        }
        return HostType.UNKNOWN;
    }

    public void deleteSite(Long id) {
        Site site = siteRepository.findOne(id);
        linkService.deleteAllBySite(site);
        siteRepository.delete(site);
    }
}
