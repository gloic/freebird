package com.gloic.freebird.webservices;

import com.gloic.freebird.persistence.model.Genre;
import com.gloic.freebird.services.service.MovieService;
import com.gloic.freebird.services.vo.request.FilterRequest;
import com.gloic.freebird.webservices.response.dto.MovieDTO;
import com.gloic.freebird.webservices.response.dto.MovieLightDTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gloic
 */
@RestController
@RequestMapping(value = "/api/movie", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MovieWebService {
    private final MovieService movieService;

    @Autowired
    public MovieWebService(MovieService movieService) {
        this.movieService = movieService;
    }

    @PutMapping(value = "/filter")
    public List<MovieLightDTO> getAllMovieFiltered(@RequestBody FilterRequest filterRequest) {
        return GenericMapper.toListLightDTO(movieService.getFilteredMovies(filterRequest));
    }

    @GetMapping(value = "/{id}")
    public MovieDTO getMovieById(@PathVariable("id") Long id) {
        return GenericMapper.toDTO(movieService.getMovieById(id));
    }

    @GetMapping(value = "/genres")
    public List<Genre> getGenres() {
        return movieService.getGenres();
    }
}
