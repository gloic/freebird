package com.gloic.freebird.services.scraper;

import com.gloic.freebird.commons.enumerations.Codec;
import com.gloic.freebird.commons.enumerations.Language;
import com.gloic.freebird.commons.enumerations.MediaCategory;
import com.gloic.freebird.commons.enumerations.Quality;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.UnknownMedia;
import com.gloic.freebird.persistence.repository.LinkRepository;
import com.gloic.freebird.persistence.repository.UnknownMediaRepository;
import com.gloic.freebird.services.parser.ItemToParse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

/**
 * Entry point for scraper. This class will call either {@link MovieScraperService} or {@link TvShowScraperService}
 *
 * @author gloic
 */
@Service
@Slf4j
class CommonScraperService {

    private final UnknownMediaRepository unknownMediaRepository;
    private final LinkRepository linkRepository;

    @Autowired
    public CommonScraperService(UnknownMediaRepository unknownMediaRepository, LinkRepository linkRepository) {
        this.unknownMediaRepository = unknownMediaRepository;
        this.linkRepository = linkRepository;
    }

    /**
     * <p>Extract tag in the filename contained in the given item to parse</p>
     * <p><b>The result will be set in the ItemToParse</b></p>
     *
     * @param itemToParse
     * @return the index of the lower tag found
     */
    public int extractTags(ItemToParse itemToParse) {
        log.debug("Extracting tags for {}", itemToParse.getFilename());

        int indexLang = extractLanguage(itemToParse);
        int indexCodec = extractCodec(itemToParse);
        int indexQuality = extractQuality(itemToParse);
        int indexYear = extractYear(itemToParse);

        int result = Integer.MAX_VALUE;

        if (indexLang != -1) {
            result = Math.min(indexLang, result);
        }
        if (indexCodec != -1) {
            result = Math.min(indexCodec, result);
        }
        if (indexQuality != -1) {
            result = Math.min(indexQuality, result);
        }

        if (indexYear != -1) {
            result = Math.min(indexYear, result);
        }

        log.debug("- Min index: {}", result);
        return result != Integer.MAX_VALUE ? result : -1;
    }

    /**
     * <p>Tries to find a <b>language</b> in the given item to parse.</p>
     * <p><b>The result will be set in the ItemToParse</b></p>
     *
     * @param itemToParse
     * @return index of the occurrence if found, else -1 will be returned
     */
    private int extractLanguage(ItemToParse itemToParse) {
        log.debug("- Looking for Language: {}", itemToParse.getFilename());
        int index;
        for (Language l : Language.values()) {
            for (String v : l.stringValues) {
                log.debug(" -Checking '{}'", v);
                index = StringUtils.indexOfIgnoreCase(itemToParse.getFilename(), v);
                if (index != -1) {
                    log.debug(" -Found: {} at index {}", v, index);
                    itemToParse.setLanguage(l);
                    return index;
                }
            }
        }
        log.debug(" -Not found");
        return -1;
    }

    /**
     * Tries to find a <b>codec</b> in the given item to parse.
     * <b>The result will be set in the ItemToParse</b>
     *
     * @param itemToParse
     * @return index of the occurrence if found, else -1 will be returned
     */
    private int extractCodec(ItemToParse itemToParse) {
        log.debug("- Looking for Codec: {}", itemToParse.getFilename());
        int index;
        for (Codec c : Codec.values()) {
            for (String v : c.stringValues) {
                log.debug(" -Checking '{}'", v);
                index = StringUtils.indexOfIgnoreCase(itemToParse.getFilename(), v);
                if (index != -1) {
                    log.debug(" -Found: {} at index {}", v, index);
                    itemToParse.setCodec(c);
                    return index;
                }
            }
        }
        log.debug(" -Not found");
        return -1;
    }

    /**
     * Tries to find a <b>quality</b> in the given item to parse.
     * <b>The result will be set in the ItemToParse</b>
     *
     * @param itemToParse
     * @return index of the occurrence if found, else -1 will be returned
     */
    private int extractQuality(ItemToParse itemToParse) {
        log.debug("- Looking for quality: {}", itemToParse.getFilename());
        int index;
        for (Quality q : Quality.values()) {
            for (String v : q.stringValues) {
                log.debug(" -Checking '{}'", v);
                index = StringUtils.indexOfIgnoreCase(itemToParse.getFilename(), v);
                if (index != -1) {
                    log.debug(" -Found: {} at index {}", v, index);
                    itemToParse.setQuality(q);
                    return index;
                }
            }
        }
        log.debug(" -Not found");
        return -1;
    }

    /**
     * Tries to find a <b>year</b> in the given item to parse.
     * <b>The result will be set in the ItemToParse</b>
     *
     * @param itemToParse
     * @return index of the occurrence if found, else -1 will be returned
     */
    private int extractYear(ItemToParse itemToParse) {
        final String filename = itemToParse.getFilename();
        Matcher matcherYear = ScraperConstants.patternTitleWithYear.matcher(filename);
        Matcher matcherStartWithYear = ScraperConstants.patternTitleStartWithYear.matcher(filename);
        if (matcherStartWithYear.find()) {
            int index = matcherStartWithYear.start();
            String year = matcherStartWithYear.group("year");
            itemToParse.setYear(Integer.valueOf(year));
            log.debug("- Filename start with year : {} at index {}", year, index);
            return index;
        } else if (matcherYear.find()) {
            int index = matcherYear.start();
            String year = matcherYear.group("year");
            itemToParse.setYear(Integer.valueOf(year));
            log.debug("- Filename contains with year : {} at index {}", year, index);
            return index;
        }
        return -1;
    }

    public Link saveToUnknownMedia(ItemToParse itemToParse) {
        return newLinkOnCategory(itemToParse, MediaCategory.UNKNOWN);
    }

    public Link newLinkOnCategory(ItemToParse itemToParse, MediaCategory mediaCategory) {
        Link link = convertToLink(itemToParse, mediaCategory);
        UnknownMedia unknownMedia = new UnknownMedia();
        unknownMedia.addLink(link);
        link.setMedia(unknownMedia);
        unknownMediaRepository.save(unknownMedia);
        return link;
    }

    public Link convertToLink(ItemToParse itemToParse, MediaCategory category) {
        Link link = new Link();
        link.setCategory(category);
        link.setUrl(itemToParse.getUrl());
        link.setSite(itemToParse.getSite());
        link.setSize(itemToParse.getSize());
        link.setFileName(itemToParse.getFilename());
        link.setParentUrl(itemToParse.getParentUrl());

        link.setCodec(itemToParse.getCodec());
        link.setQuality(itemToParse.getQuality());
        link.setLanguage(itemToParse.getLanguage());

        linkRepository.save(link);
        return link;
    }

    /**
     * <p>Check if the given string match with a media file</p>
     * <p>The check is based on a regex that research the file extension</p>
     *
     * @param fileName
     * @return true if the file name is a media
     */
    public boolean isMedia(String fileName) {
        return ScraperConstants.patternMedia.matcher(fileName).find();
    }

    /**
     * <p>Check if the given filename matches with the pattern of an episode</p>
     * <p>The check is based on a regex that research for specific pattern.</p>
     * For more detail, see the regex itself: {@link ScraperConstants#patternTvShow}
     *
     * @param filename
     * @return true if the string looks like to a tv show episode
     */
    public boolean isAnEpisode(String filename) {
        return ScraperConstants.patternTvShow.matcher(filename).find();
    }

}

