package com.gloic.freebird.webservices;

import com.gloic.freebird.services.service.DashboardService;
import com.gloic.freebird.webservices.response.dto.EpisodeDTO;
import com.gloic.freebird.webservices.response.dto.MovieLightDTO;
import com.gloic.freebird.webservices.response.dto.TvShowLightDTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gloic
 */
@RestController
@RequestMapping(value = "/api/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DashboardWebService {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardWebService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "/search/tv/{term}")
    public List<TvShowLightDTO> searchTvShow(@PathVariable("term") String term) {
        return GenericMapper.toListLightDTO(dashboardService.searchTvShow(term));
    }

    @GetMapping(value = "/search/movie/{term}")
    public List<MovieLightDTO> searchMovie(@PathVariable("term") String term) {
        return GenericMapper.toListLightDTO(dashboardService.searchMovie(term));
    }

    @GetMapping(value = "/top/movies")
    public List<MovieLightDTO> findTopMovies() {
        return GenericMapper.toListLightDTO(dashboardService.findTopMovies());
    }

    @GetMapping(value = "/top/episodes")
    public List<EpisodeDTO> findTopEpisodes() {
        return GenericMapper.toListDTO(dashboardService.findTopEpisodes());
    }
}
