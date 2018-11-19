package com.gloic.freebird.services.scraper;

import com.gloic.freebird.commons.enumerations.MediaCategory;
import com.gloic.freebird.commons.util.AppStringUtil;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.persistence.repository.MovieRepository;
import com.gloic.freebird.services.parser.ItemToParse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Scraper for movies
 *
 * @author gloic
 */
@Slf4j
@Service
public class MovieScraperService {

    private final TheMovieDBService theMovieDBService;
    private final MovieRepository movieRepository;
    private final CommonScraperService commonScraperService;

    @Value("${scraper.movie.titleMinLength}")
    private Integer movieTitleMinLength;

    @Autowired
    public MovieScraperService(TheMovieDBService theMovieDBService, MovieRepository movieRepository, CommonScraperService commonScraperService) {
        this.theMovieDBService = theMovieDBService;
        this.movieRepository = movieRepository;
        this.commonScraperService = commonScraperService;
    }

    /**
     * Scrape data for a movie
     *
     * @param itemToParse
     */
    public void scrape(ItemToParse itemToParse) {
        if (itemToParse.getFilename().length() < movieTitleMinLength) {
            commonScraperService.saveToUnknownMedia(itemToParse);
            return;
        }

        // Find tag and potential title
        extractData(itemToParse);

        // request API to find the movie
        Movie movie = searchMovie(itemToParse);

        if (movie != null) {
            // Check if the movie is already in DB
            Movie inDb = movieRepository.findByTitle(movie.getTitle());
            Link link = commonScraperService.convertToLink(itemToParse, MediaCategory.MOVIE);
            if (inDb != null) {
                // Add a link to existing movie
                if (!itemToParse.isIdentification()) {
                    inDb.addLink(link);
                    link.setMedia(inDb);

                }
                movieRepository.save(inDb);
            } else {
                // persist the new movie
                if (!itemToParse.isIdentification()) {
                    movie.addLink(link);
                    link.setMedia(movie);
                }
                movieRepository.save(movie);
            }
        } else {
            // if not found, store it as unknown
            commonScraperService.saveToUnknownMedia(itemToParse);
        }
    }

    /**
     * <ul>
     * <li>Read the filename in order to extract the possible TAG</li>
     * <li>Clean the string</li>
     * <li>Set the result on given ItemToParse</li>
     * </ul>
     *
     * @param itemToParse
     */
    void extractData(ItemToParse itemToParse) {
        String filename = itemToParse.getFilename();
        log.debug("Multi search for: '{}'", filename);
        int tagIndex = commonScraperService.extractTags(itemToParse);
        String potentialTitle;

        if (tagIndex > 0) {
            potentialTitle = itemToParse.getFilename().substring(0, tagIndex);
            log.debug("- Substring from begin to start of tag: '{}'", potentialTitle);
        } else {
            potentialTitle = itemToParse.getFilename();
        }
        potentialTitle = AppStringUtil.cleanString(potentialTitle);

        if (StringUtils.isEmpty(potentialTitle)) {
            log.warn("potentialTitle has been destroyed by matchers! Filename:{}", filename);
        }

        itemToParse.setPotentialTitle(potentialTitle);
    }

    /**
     * call moviedbapi to retrieve movie's details
     *
     * @param itemToParse
     * @return
     */
    public Movie searchMovie(ItemToParse itemToParse) {
        if (StringUtils.isEmpty(itemToParse.getPotentialTitle())) return null;

        Movie movie = theMovieDBService.multiSearch(itemToParse);
        if (movie != null) {
            log.debug("Movie found:{}", movie);
            return movie;
        }
        return null;
    }
}
