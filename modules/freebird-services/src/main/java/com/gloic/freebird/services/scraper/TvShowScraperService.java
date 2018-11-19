package com.gloic.freebird.services.scraper;


import com.gloic.freebird.commons.enumerations.MediaCategory;
import com.gloic.freebird.commons.util.AppStringUtil;
import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Season;
import com.gloic.freebird.persistence.model.TvShow;
import com.gloic.freebird.persistence.repository.EpisodeRepository;
import com.gloic.freebird.persistence.repository.SeasonRepository;
import com.gloic.freebird.persistence.repository.TvShowRepository;
import com.gloic.freebird.services.parser.ItemToParse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

/**
 * The scraper tries to find data from a supposed tv show episode.
 *
 * @author gloic
 */
@Service
@Slf4j
public class TvShowScraperService {

    private final TvShowRepository tvShowRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final TheMovieDBService theMovieDBService;
    private final CommonScraperService commonScraperService;

    @Autowired
    public TvShowScraperService(TvShowRepository tvShowRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository, TheMovieDBService theMovieDBService, CommonScraperService commonScraperService) {
        this.tvShowRepository = tvShowRepository;
        this.seasonRepository = seasonRepository;
        this.episodeRepository = episodeRepository;
        this.theMovieDBService = theMovieDBService;
        this.commonScraperService = commonScraperService;
    }

    /**
     * Entry point of the service.
     *
     * @param itemToParse
     */
    public void scrape(ItemToParse itemToParse) {
        // Complete item to parse with meta data
        extractData(itemToParse);
        String potentialTitle = itemToParse.getPotentialTitle();

        if (StringUtils.isEmpty(potentialTitle)) {
            log.error("Cleaner or matchers destroyed filename! : {}", itemToParse.getFilename());
            commonScraperService.saveToUnknownMedia(itemToParse);
            return;
        }

        // trying to find data from themoviedbapi
        getEpisode(itemToParse, potentialTitle);
    }

    /**
     * Complete ItemToParse with metadata
     * <ul>
     * <li>Tv show, Season number and episode number</li>
     * <li>Data from tags, like quality, language and codec</li>
     * </ul>
     *
     * @param itemToParse
     */
    void extractData(ItemToParse itemToParse) {
        Matcher matcherStartTvEpisode = ScraperConstants.patternTvShowStart.matcher(itemToParse.getFilename());
        if (matcherStartTvEpisode.find()) {
            log.debug("Filename start by episode tag:{}", itemToParse.getFilename());
            // TODO
        }

        // This regex was already check previously. Just using it to extract data
        Matcher matcherTvEpisode = ScraperConstants.patternTvShow.matcher(itemToParse.getFilename());
        matcherTvEpisode.find();

        final String filename = itemToParse.getFilename();
        int indexSubString = matcherTvEpisode.start();
        int indexTag = commonScraperService.extractTags(itemToParse);
        if (indexTag != -1) {
            indexSubString = Math.min(indexTag, indexSubString);
        }

        String showName = filename.substring(0, indexSubString);
        itemToParse.setPotentialTitle(AppStringUtil.cleanString(showName));

        int seasonNum = Integer.parseInt(matcherTvEpisode.group("season"));
        int episodeNum = Integer.parseInt(matcherTvEpisode.group("episode"));
        itemToParse.setSeasonNum(seasonNum);
        itemToParse.setEpisodeNum(episodeNum);

        log.debug("FileName:{}", filename);
        log.debug(" -Show Name:{}", showName);
        log.debug(" -Season:{}", seasonNum);
        log.debug(" -Episode:{}", episodeNum);
    }

    /**
     * @param itemToParse
     * @param showName
     * @return
     */
    public Episode getEpisode(ItemToParse itemToParse, String showName) {

        // Find show if possible
        final TvShow tvShow = getTvShow(showName);
        if (tvShow == null) {
            log.debug("TVShow not found. Assigning to unknown. '{}'", showName);
            commonScraperService.saveToUnknownMedia(itemToParse);
            return null;
        }

        final int seasonNum = itemToParse.getSeasonNum();
        final int episodeNum = itemToParse.getEpisodeNum();

        // Check if episodeNum is in DB
        Episode episode = episodeRepository.findByEpisodeNumAndSeasonSeasonNumberAndSeasonTvShow(episodeNum, seasonNum, tvShow);
        if (episode != null) {
            log.debug("Episode in DB, adding a link");
            if (!itemToParse.isIdentification()) {
                Link link = commonScraperService.convertToLink(itemToParse, MediaCategory.TV_SHOW);
                episode.addLink(link);
                link.setMedia(episode);
            }
            episodeRepository.save(episode);
            return episode;
        }

        // find season if possible
        Season season = getSeason(seasonNum, tvShow);
        if (season == null) {
            log.debug("Season {} not found for TVShow '{}'. Assigning to unknown", seasonNum, tvShow.getTitle());
            commonScraperService.saveToUnknownMedia(itemToParse);
            return null;
        }

        // retrieve info from API
        episode = theMovieDBService.getEpisodeInfo(tvShow, seasonNum, episodeNum);
        if (episode == null) {
            log.debug("Episode info not found. Assigning to unknown. Episode: {}, Season:{} for TVShow: '{}'", new Object[]{episodeNum, seasonNum, tvShow.getTitle()});
            commonScraperService.saveToUnknownMedia(itemToParse);
            return null;
        }

        if (!itemToParse.isIdentification()) {
            // persist a new Link, only if it's not an identification.
            // otherwise, the Link will be re-assigned
            Link link = commonScraperService.convertToLink(itemToParse, MediaCategory.TV_SHOW);
            episode.addLink(link);
            link.setMedia(episode);
        }

        // persist all
        episode.setSeason(season);
        episodeRepository.save(episode);
        season.addEpisode(episode);
        seasonRepository.save(season);
        tvShowRepository.save(tvShow);

        return episode;
    }

    /**
     * Retrieve a season from its number and the tv show title.
     * Check in DB. if not, ask to API
     *
     * @param seasonNum
     * @param tvShow
     * @return a Season or null if not found
     */
    private Season getSeason(int seasonNum, TvShow tvShow) {
        // TODO use getter from TVShow instead of a query...
        Season season = seasonRepository.findByTvShowAndSeasonNumber(tvShow, seasonNum);
        // Is Season already in DB?
        if (season != null) return season;

        //  Season not yet registered, find info from API
        season = theMovieDBService.getSeasonInfo(tvShow, seasonNum);
        if (season != null) {
            season.setTvShow(tvShow);
//            seasonRepository.save(season);
            tvShow.addSeason(season);
//            tvShowRepository.save(tvShow);
        }

        return season;
    }

    /**
     * Tries to find a TvShow from its title:
     * <ul>
     * <li>check if the title match with a stored alias</li>
     * <li>if not, retrieve the idShow (provided by API)</li>
     * <li>request tv show id in DB</li>
     * <li>if found, then add an alias and return the tv show</li>
     * </ul>
     *
     * @param showName
     * @return a TvShow or null if not found
     */
    private synchronized TvShow getTvShow(String showName) {
        //Check if the tv show is in DB
        TvShow tvShow = tvShowRepository.findByAliasesIn(showName);
        // Tv show already in DB (search by alias)
        if (tvShow != null) return tvShow;

        // Retrieve tvShow from API
        tvShow = theMovieDBService.searchTVShow(showName);
        if (tvShow == null) return null;

        // check if the show is not in db
        TvShow fromDB = tvShowRepository.findByIdShow(tvShow.getIdShow());
        if (fromDB != null) {
            tvShow = fromDB;
        }

        // Add an alias and update
        tvShow.getAliases().add(showName);
        tvShowRepository.save(tvShow);
        return tvShow;
    }
}
