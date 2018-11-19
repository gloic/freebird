package com.gloic.freebird.webservices;

import com.gloic.freebird.persistence.model.Genre;
import com.gloic.freebird.services.service.TvShowService;
import com.gloic.freebird.services.vo.request.FilterRequest;
import com.gloic.freebird.webservices.response.dto.EpisodeDTO;
import com.gloic.freebird.webservices.response.dto.SeasonDTO;
import com.gloic.freebird.webservices.response.dto.TvShowDTO;
import com.gloic.freebird.webservices.response.dto.TvShowLightDTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(value = "/api/tvshow", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TvShowWebService {

    private final TvShowService tvShowService;

    public TvShowWebService(TvShowService tvShowService) {
        this.tvShowService = tvShowService;
    }

    @PutMapping(value = "/filter")
    public List<TvShowLightDTO> getAllTvShowsFiltered(@RequestBody FilterRequest filterRequest) {
        return GenericMapper.toListLightDTO(tvShowService.getFilteredTvShows(filterRequest));
    }

    @GetMapping(value = "/{id}")
    public TvShowDTO getDetailById(@PathVariable("id") Long id) {
        return GenericMapper.toDTO(tvShowService.getDetailById(id));
    }

    @GetMapping(value = "/season/{id}")
    public SeasonDTO getSeasonDetail(@PathVariable("id") Long id) {
        return GenericMapper.toDTO(tvShowService.getSeasonDetail(id));
    }

    @GetMapping(value = "/episode/{id}")
    public EpisodeDTO getEpisodeDetail(@PathVariable("id") Long id) {
        return GenericMapper.toDTO(tvShowService.getEpisodeDetail(id));
    }

    @GetMapping(value = "/genres")
    public List<Genre> getGenres() {
        return tvShowService.getGenres();
    }
}
