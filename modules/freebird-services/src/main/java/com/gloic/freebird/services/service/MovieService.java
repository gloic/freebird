package com.gloic.freebird.services.service;

import com.gloic.freebird.persistence.model.Genre;
import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.persistence.model.Movie_;
import com.gloic.freebird.persistence.repository.MovieRepository;
import com.gloic.freebird.services.specification.MovieSpecs;
import com.gloic.freebird.services.vo.request.FilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage movies
 *
 * @author gloic
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getFilteredMovies(FilterRequest filterRequest) {
        Specifications<Movie> movieSpecifications = Specifications.where(MovieSpecs.preferenceSpecs()).and(MovieSpecs.byFilters(filterRequest));

        Sort sort;
        if (filterRequest.getOrderBy() != null) {
            switch (filterRequest.getOrderBy()) {
                case "alphabetic":
                    sort = MovieSpecs.SORT_BY_TITLE_ASC;
                    break;
                case "releaseDate":
                    sort = new Sort(Sort.Direction.DESC, Movie_.releaseDate.getName());
                    break;
                case "addingDate":
                    sort = new Sort(Sort.Direction.DESC, Movie_.created.getName());
                    break;
                case "popularity":
                    sort = new Sort(Sort.Direction.DESC, Movie_.popularity.getName());
                    break;
                default:
                    sort = MovieSpecs.SORT_BY_TITLE_ASC;
                    break;
            }
        } else {
            sort = MovieSpecs.SORT_BY_TITLE_ASC;
        }
        Pageable pageable = new PageRequest(0, 20, sort);
        return movieRepository.findAll(movieSpecifications, pageable).getContent();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findOne(id);
    }

    /**
     * @return all genre linked to at least one movie
     */
    public List<Genre> getGenres() {
        return movieRepository.findAllGenres();
    }
}
