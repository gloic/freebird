package com.gloic.freebird.services.scraper;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Genre;
import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.persistence.model.Season;
import com.gloic.freebird.persistence.model.TvShow;
import com.gloic.freebird.persistence.repository.GenreRepository;
import com.gloic.freebird.services.parser.ItemToParse;
import com.gloic.freebird.services.scraper.mapper.GenreAPIToGenre;
import com.gloic.freebird.services.scraper.mapper.MovieBasicToMovie;
import com.gloic.freebird.services.scraper.mapper.TVBasicToTVShow;
import com.gloic.freebird.services.scraper.mapper.TVEpisodeInfoToEpisode;
import com.gloic.freebird.services.scraper.mapper.TVSeasonInfoToSeason;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo;
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo;
import com.omertron.themoviedbapi.results.ResultList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service that call TheMovieDbApi and convert the responses in local model
 *
 * @author gloic
 */
@Service
@Slf4j
public final class TheMovieDBService {

    @Value("${themoviedatabase.apikey.v3}")
    private String apiKey;

    @Value("${themoviedatabase.lang}")
    private String language;

    @Autowired
    private TheMovieDbApi theMovieDbApi;

    @Autowired
    private GenreRepository genreRepository;

    // Genres are lazily initialized
    @Getter(lazy = true)
    private final Map<Integer, Genre> genres = initGenres();

    /**
     * Search a TvShow from its title
     *
     * @param query
     * @return a TvShow object
     */
    public TvShow searchTVShow(String query) {
        try {
            ResultList<TVBasic> resultList = theMovieDbApi.searchTV(query, 1, language, null, null);
            if (!resultList.isEmpty()) {
                TVBasic tvBasic = resultList.getResults().iterator().next();
                // Convert response to local persistent
                TvShow tvShow = new TVBasicToTVShow().apply(tvBasic);
                // Bind genres
                tvShow.setGenres(tvBasic.getGenreIds().stream().map(g -> getGenres().get(g)).collect(Collectors.toList()));
                return tvShow;
            }
        } catch (MovieDbException e) {
            log.warn("No information found for Tv show. Query:{)", query);
        }

        return null;
    }

    /**
     * Find Season from API
     *
     * @param tvShow
     * @param num
     * @return
     */
    public Season getSeasonInfo(TvShow tvShow, Integer num) {
        try {
            TVSeasonInfo seasonInfo = theMovieDbApi.getSeasonInfo(tvShow.getIdShow(), num, language);
            if (seasonInfo != null) {
                // transform response to local persistent
                return new TVSeasonInfoToSeason().apply(seasonInfo);
            }
        } catch (MovieDbException e) {
            log.warn("No information found for season {} of '{}'", num, tvShow.getTitle());
        }
        return null;
    }

    /**
     * Find Episode from API
     *
     * @param tvShow
     * @param season
     * @param episode
     * @return
     */
    public Episode getEpisodeInfo(TvShow tvShow, Integer season, Integer episode) {
        try {
            TVEpisodeInfo episodeInfo = theMovieDbApi.getEpisodeInfo(tvShow.getIdShow(), season, episode, language);
            if (episodeInfo != null) {
                return new TVEpisodeInfoToEpisode().apply(episodeInfo);
            }
        } catch (MovieDbException e) {
            log.warn("No information found for episode. Season:{}, Episode:{}, tvShow:{}", season, episode, tvShow.getTitle());
        }

        return null;

    }

    /**
     * Search a movie from its title
     *
     * @param itemToParse
     * @return
     */
    public Movie multiSearch(ItemToParse itemToParse) {
        String query = itemToParse.getPotentialTitle();
        if (query == null || query.isEmpty()) {
            return null;
        }
        try {
            ResultList<MovieInfo> result = theMovieDbApi.searchMovie(query, null, language, false, itemToParse.getYear() != null ? itemToParse.getYear() : 0, null, null);
            if (!result.isEmpty()) {
                MovieInfo movieInfo = result.getResults().get(0);
                Movie movie = new MovieBasicToMovie().apply(movieInfo);
                // Bind genres
                movie.setGenres(movieInfo.getGenreIds().stream().map(g -> getGenres().get(g)).collect(Collectors.toList()));
                return movie;
            } else {
                log.debug("No result for query : {}", query);
            }
        } catch (MovieDbException e) {
            log.error("Cannot instantiate TheMovieDbApi", e);
        } catch (Exception e) {
            log.error("fkdslmkfdslm");
        }

        return null;
    }

    /**
     * Get all genre from API. Used at first server's start, see {@link com.gloic.freebird.services.config.InstallationConfiguration}
     *
     * @return a list of Genre
     */
    public Set<Genre> getAllGenreFromAPI() {
        Set<Genre> genres = new HashSet<>();
        try {
            ResultList<com.omertron.themoviedbapi.model.Genre> genreTVList = theMovieDbApi.getGenreTVList(language);
            if (genreTVList != null && genreTVList.getResults() != null) {
                genres.addAll(genreTVList.getResults().stream().map(g -> new GenreAPIToGenre().apply(g)).collect(Collectors.toList()));
            }

            ResultList<com.omertron.themoviedbapi.model.Genre> genreMovieList = theMovieDbApi.getGenreMovieList(language);
            if (genreMovieList != null && genreMovieList.getResults() != null) {
                genres.addAll(genreMovieList.getResults().stream().map(g -> new GenreAPIToGenre().apply(g)).collect(Collectors.toList()));
            }
        } catch (MovieDbException e) {
            log.error("Cannot retrieve all genre!", e);
        }
        return genres;
    }

    @Bean
    public TheMovieDbApi theMovieDbApi() throws MovieDbException {
        if (apiKey.equals("YOUR_API_KEY")) {
            throw new RuntimeException("You need to configure you API key v3. Create an account on https://www.themoviedb.org/. " +
                    "For more information, consult the documentation");
        }
        return new TheMovieDbApi(apiKey);
    }

    /**
     * Lazy method to retrieve all genre from DB
     *
     * @return
     */
    private Map<Integer, Genre> initGenres() {
        return genreRepository.findAll().stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
    }
}
