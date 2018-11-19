package com.gloic.freebird.services.scraper;

import com.gloic.freebird.commons.util.AppStringUtil;
import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.persistence.model.UnknownMedia;
import com.gloic.freebird.persistence.repository.UnknownMediaRepository;
import com.gloic.freebird.services.parser.ItemToParse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ScraperService {

    private final CommonScraperService commonScraperService;
    private final TvShowScraperService tvShowScraperService;
    private final MovieScraperService movieScraperService;

    private final UnknownMediaRepository unknownMediaRepository;

    @Autowired
    public ScraperService(CommonScraperService commonScraperService, TvShowScraperService tvShowScraperService, MovieScraperService movieScraperService, UnknownMediaRepository unknownMediaRepository) {
        this.commonScraperService = commonScraperService;
        this.tvShowScraperService = tvShowScraperService;
        this.movieScraperService = movieScraperService;
        this.unknownMediaRepository = unknownMediaRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void scrape(ItemToParse itemToParse) {
        final String filename = itemToParse.getFilename();
        itemToParse.setFilename(AppStringUtil.removeExtension(filename));
        // Is Tv Show
        if (commonScraperService.isAnEpisode(filename)) {
            tvShowScraperService.scrape(itemToParse);
        } else {
            movieScraperService.scrape(itemToParse);
        }
    }

    public boolean isMedia(String filename) {
        return commonScraperService.isMedia(filename);
    }

    public Episode getEpisode(ItemToParse itemToParse, int seasonNum, int episodeNum, String showName) {
        itemToParse.setSeasonNum(seasonNum);
        itemToParse.setEpisodeNum(episodeNum);
        return tvShowScraperService.getEpisode(itemToParse, showName);
    }

    public Movie searchMovie(ItemToParse itemToParse) {
        return movieScraperService.searchMovie(itemToParse);
    }

    /**
     * Build an item to parse from an unknown media
     *
     * @param media
     * @return
     */
    public ItemToParse unknownMediaToItemToParse(UnknownMedia media) {
        return new ItemToParse().toBuilder()
                .url(media.getLink().getUrl())
                .site(media.getLink().getSite())
                .size(media.getLink().getSize())
                .isIdentification(true)
                .filename(media.getLink().getFileName())
                .build();
    }
}
