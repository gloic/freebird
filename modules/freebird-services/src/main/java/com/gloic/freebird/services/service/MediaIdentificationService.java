package com.gloic.freebird.services.service;

import com.gloic.freebird.commons.enumerations.MediaCategory;
import com.gloic.freebird.commons.util.AppStringUtil;
import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.persistence.model.Site;
import com.gloic.freebird.persistence.model.UnknownMedia;
import com.gloic.freebird.persistence.repository.EpisodeRepository;
import com.gloic.freebird.persistence.repository.LinkRepository;
import com.gloic.freebird.persistence.repository.MovieRepository;
import com.gloic.freebird.persistence.repository.SiteRepository;
import com.gloic.freebird.persistence.repository.UnknownMediaRepository;
import com.gloic.freebird.services.parser.ItemToParse;
import com.gloic.freebird.services.scraper.ScraperConstants;
import com.gloic.freebird.services.scraper.ScraperService;
import com.gloic.freebird.services.vo.request.EpisodeIdentificationRequest;
import com.gloic.freebird.services.vo.request.MovieIdentificationRequest;
import com.gloic.freebird.services.vo.request.SeasonIdentificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Service that manage the media identification
 *
 * @author gloic
 */
@Service
@Slf4j
public class MediaIdentificationService {

    private final ScraperService scraperService;
    private final UnknownMediaRepository unknownMediaRepository;

    private final LinkRepository linkRepository;
    private final MovieRepository movieRepository;
    private final EpisodeRepository episodeRepository;
    private final SiteRepository siteRepository;


    @Autowired
    public MediaIdentificationService(ScraperService scraperService, UnknownMediaRepository unknownMediaRepository, LinkRepository linkRepository, MovieRepository movieRepository, EpisodeRepository episodeRepository, SiteRepository siteRepository) {
        this.scraperService = scraperService;
        this.unknownMediaRepository = unknownMediaRepository;
        this.linkRepository = linkRepository;
        this.movieRepository = movieRepository;
        this.episodeRepository = episodeRepository;
        this.siteRepository = siteRepository;
    }

    public List<UnknownMedia> findAllUnknown(int page) {
        Pageable pageable = new PageRequest(page, 100);
        return unknownMediaRepository.findAllByLinksCategoryOrderByLinksFileNameAsc(MediaCategory.UNKNOWN, pageable);
    }

    /**
     * Identify an episode with precises information
     * <ul>
     * <li>tv show name</li>
     * <li>Season number</li>
     * <li>Episode number</li>
     * </ul>
     *
     * @param request
     * @return
     */
    public boolean identifyEpisode(EpisodeIdentificationRequest request) {
        // TODO check if is media still online
        UnknownMedia media = unknownMediaRepository.findOne(request.getMediaId());
        ItemToParse itemToParse = scraperService.unknownMediaToItemToParse(media);
        Episode episode = scraperService.getEpisode(itemToParse, request.getSeasonNum(), request.getEpisodeNum(), request.getShowName());
        if (episode != null) {
            media.getLink().setCategory(MediaCategory.TV_SHOW);
            episode.addLink(media.getLink());
            media.getLink().setMedia(episode);
            episodeRepository.save(episode);
            unknownMediaRepository.delete(media);
            return true;
        }
        unknownMediaRepository.delete(media);
        return false;
    }

    /**
     * Identify a whole season with given information:
     * <ul>
     * <li>Tv show name</li>
     * <li>season number</li>
     * </ul>
     * <p><b>Very unstable identification method, consider it as BETA functionality</b></p>
     * <p>The difficulty here is to find the episode number.</p>
     * The used methods are:
     * <ul>
     * <li>If the filename contains a standard tag for tv show (S01E02) , the episode number will be easily retrieved.</li>
     * <li>If not, looking after the <b>first</b> number characters in the filename. As security, if this numeric value contain the season number, only the rest will be kept.</li>
     * </ul>
     * With this technique, the episode number can be found in these cases : (given Season number = 1)
     * <ul>
     * <li>Episode.S01E02.Some.Text</li>
     * <li>S01E02.Some.Text</li>
     * <li>102</li>
     * <li>Episode.102</li>
     * </ul>
     * <p>This method is dangerous because if the given season name is "1",
     * the pattern "Episode.0102" will fail and "14_Episode.102" will fail too because "14" will be considered as episode number</p>
     *
     * @param request
     * @return
     */
    public boolean identifySeason(SeasonIdentificationRequest request) {
        // TODO check if is media still online
        UnknownMedia media = unknownMediaRepository.findOne(request.getMediaId());
        Link link = media.getLink();
        List<UnknownMedia> otherMedias = unknownMediaRepository.findDistinctByLinksSiteAndLinksParentUrl(link.getSite(), link.getParentUrl());
        otherMedias.forEach(m -> {
            Matcher matcher = ScraperConstants.patternTvShow.matcher(AppStringUtil.cleanString(m.getLink().getFileName()));
            Integer episodeNum;
            if (matcher.find()) {
                episodeNum = Integer.valueOf(matcher.group("episode"));
            } else {
                episodeNum = AppStringUtil.findFirstNumeric(m.getLink().getFileName());
                if (episodeNum != null && episodeNum.toString().contains(request.getSeasonNum().toString()) && episodeNum.toString().length() > request.getSeasonNum().toString().length()) {
                    // Kids, don't try this at home
                    episodeNum = Integer.valueOf(episodeNum.toString().replaceFirst(request.getSeasonNum().toString(), ""));
                }
            }

            if (episodeNum != null) {
                EpisodeIdentificationRequest episodeIdentificationRequest = new EpisodeIdentificationRequest(m.getId(), request.getShowName(), request.getSeasonNum(), episodeNum);
                this.identifyEpisode(episodeIdentificationRequest);
            }
        });
        return true;
    }

    /**
     * Rescan an unknown media. Mainly used when the parser and scraper are modified
     *
     * @param media
     */
    public void rescan(UnknownMedia media) {
        // TODO check if is media still online
        ItemToParse itemToParse = scraperService.unknownMediaToItemToParse(media);
        scraperService.scrape(itemToParse);
    }

    /**
     * Send a movie to the unknown category
     *
     * @param movieId
     * @param linkId
     */
    public void wrongMovieIdentification(Long movieId, Long linkId) {
        // TODO check if is media still online
        attachLinkToUnknown(linkId);

        Movie movie = movieRepository.findOne(movieId);
        List<Link> links = movie.getLinks();

        if (links.size() > 1) {
            // Remove a link
            for (int i = 0; i < links.size(); i++) {
                if (links.get(i).getId().equals(linkId)) {
                    links.remove(i);
                    break;
                }
            }
            movieRepository.save(movie);
        } else {
            // Delete the movie if no link left
            movieRepository.delete(movie);
        }
    }

    public void wrongEpisodeIdentification(Long episodeId, Long linkId) {
        // TODO check if is media still online
        attachLinkToUnknown(linkId);

        /*
        TODO remove episode, season or tv show if there are some oprhan data
        // Clean hierarchy
        Season season = episode.getSeason();
        if(season.getEpisodes().size() == 1) {
            // Delete season
            log.debug("Season needs to be deleted");
            if(season.getTvShow().getSeasons().size() == 1) {
                // Delete tv show
                log.debug("Tv show needs to be deleted");
            }
        }
        */
        episodeRepository.delete(episodeId);
    }

    private void attachLinkToUnknown(Long linkId) {
        Link link = linkRepository.findOne(linkId);
        link.setCategory(MediaCategory.UNKNOWN);
        UnknownMedia media = new UnknownMedia();
        media.addLink(link);
        link.setMedia(media);
        unknownMediaRepository.save(media);
    }

    public void ignoreMedia(Long id) {
        UnknownMedia media = unknownMediaRepository.findOne(id);
        media.setIgnored(true);
        unknownMediaRepository.save(media);
    }

    /**
     * Ignore a complete folder of a site.
     * Can be useful if a folder contains scrap or video clips
     *
     * @param id
     */
    public void ignoreFolder(Long id) {
        UnknownMedia media = unknownMediaRepository.findOne(id);
        Link link = media.getLink();
        Site site = link.getSite();
        site.addIgnoredUrl(link.getParentUrl());
        siteRepository.save(site);
        unknownMediaRepository.deleteByLinksParentUrl(link.getParentUrl());
        linkRepository.delete(link);
    }

    /**
     * Identify an unknown media to a movie
     *
     * @param request
     * @return
     */
    public boolean identifyMovie(MovieIdentificationRequest request) {
        UnknownMedia media = unknownMediaRepository.findOne(request.getMediaId());
        ItemToParse itemToParse = scraperService.unknownMediaToItemToParse(media);
        itemToParse.setPotentialTitle(request.getTitle());
        Movie result = scraperService.searchMovie(itemToParse);

        if (result != null) {
            // Change category
            media.getLink().setCategory(MediaCategory.MOVIE);
            // Assign link to movie
            result.getLinks().add(media.getLink());
            // Save movie
            movieRepository.save(result);
            // Delete unknown
            unknownMediaRepository.delete(media);
            return true;
        }
        return false;
    }
}