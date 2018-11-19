package com.gloic.freebird.services.scraper.mapper;

import com.gloic.freebird.persistence.model.Movie;
import com.omertron.themoviedbapi.model.movie.MovieInfo;
import org.springframework.data.rest.core.util.Function;

/**
 * Map the result of API to a local persistent
 *
 * @author gloic
 */
public class MovieBasicToMovie implements Function<MovieInfo, Movie> {
    @Override
    public Movie apply(MovieInfo input) {
        Movie output = new Movie();
        output.setTitle(input.getTitle());
        output.setOverview(input.getOverview());
        output.setReleaseDate(input.getReleaseDate());
        output.setPosterPath(input.getPosterPath());
        output.setPopularity(input.getPopularity());
        return output;
    }
}
