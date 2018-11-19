package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.webservices.response.mapper.DTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.Data;

/**
 * MovieDetailVO extends MovieLightVO and complete it with more data
 *
 * @author gloic
 */
@DTO(DtoOf = Movie.class)
@Data
public final class MovieDTO extends MovieLightDTO {

    private String overview;
    private String url;
    private String releaseDate;

    public MovieDTO(Movie movie) {
        super(movie);
        this.overview = movie.getOverview();
        this.releaseDate = movie.getReleaseDate();
        this.links = GenericMapper.toListDTO(movie.getLinks());
    }
}
