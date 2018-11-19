package com.gloic.freebird.services.scraper.mapper;

import com.omertron.themoviedbapi.model.Genre;
import org.springframework.data.rest.core.util.Function;

/**
 * Map the result of API to a local persistent
 *
 * @author gloic
 */
public class GenreAPIToGenre implements Function<Genre, com.gloic.freebird.persistence.model.Genre> {
    @Override
    public com.gloic.freebird.persistence.model.Genre apply(Genre input) {
        com.gloic.freebird.persistence.model.Genre genre = new com.gloic.freebird.persistence.model.Genre();
        genre.setId(input.getId());
        genre.setName(input.getName());
        return genre;
    }
}
