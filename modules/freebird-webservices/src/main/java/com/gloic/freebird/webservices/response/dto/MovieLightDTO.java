package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.Movie;
import com.gloic.freebird.webservices.response.mapper.DTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.Data;

import java.util.List;

/**
 * MovieLightVO contains minimal information to be displayed in a grid or a list
 *
 * @author gloic
 */
@DTO(DtoOf = Movie.class, light = true)
@Data
public class MovieLightDTO {

    private Long id;
    private String title;
    private String posterPath;
    protected List<? extends LinkLightDTO> links;

    public MovieLightDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.posterPath = movie.getPosterPath();
        this.links = GenericMapper.toListDTO(movie.getLinks());
    }
}
