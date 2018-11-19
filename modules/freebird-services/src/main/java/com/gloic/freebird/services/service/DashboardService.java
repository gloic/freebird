package com.gloic.freebird.services.service;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.Episode_;
import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.persistence.model.Movie_;
import com.gloic.freebird.persistence.model.TvShow;
import com.gloic.freebird.persistence.repository.EpisodeRepository;
import com.gloic.freebird.persistence.repository.MovieRepository;
import com.gloic.freebird.persistence.repository.TvShowRepository;
import com.gloic.freebird.services.specification.EpisodeSpecs;
import com.gloic.freebird.services.specification.MovieSpecs;
import com.gloic.freebird.services.specification.TvShowSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Service for the Dashboard
 *
 * @author gloic
 */
@Service
public class DashboardService {

    private final TvShowRepository tvShowRepository;
    private final MovieRepository movieRepository;
    private final EpisodeRepository episodeRepository;

    @Autowired
    public DashboardService(TvShowRepository tvShowRepository, MovieRepository movieRepository, EpisodeRepository episodeRepository) {
        this.tvShowRepository = tvShowRepository;
        this.movieRepository = movieRepository;
        this.episodeRepository = episodeRepository;
    }

    /**
     * Search a tv show by its title (and user's preferences)
     *
     * @param term
     * @return
     */
    public List<TvShow> searchTvShow(String term) {
        return
                tvShowRepository.findAll(
                        where(
                                TvShowSpecs.preferenceSpecs()
                        ).and(
                                TvShowSpecs.byTitle(term)
                        ),
                        TvShowSpecs.SORT_BY_TITLE_ASC
                );
    }

    /**
     * Search a movie by its title (and user's preferences)
     *
     * @param term
     * @return
     */
    public List<Movie> searchMovie(String term) {
        return movieRepository.findAll(
                where(
                        MovieSpecs.preferenceSpecs()
                ).and(
                        MovieSpecs.byTitle(term)
                ),
                MovieSpecs.SORT_BY_TITLE_ASC
        );
    }

    /**
     * Find last <b>released</b> movies
     *
     * @return
     */
    public List<Movie> findTopMovies() {
        Pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, Movie_.releaseDate.getName()));
        return movieRepository.findAll(MovieSpecs.preferenceSpecs(), pageable).getContent();
    }

    /**
     * Find last <b>released</b> episodes
     *
     * @return
     */
    public List<Episode> findTopEpisodes() {
        Pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, Episode_.airDate.getName()));
        return episodeRepository.findAll(EpisodeSpecs.preferenceSpecs(), pageable).getContent();
    }
}
