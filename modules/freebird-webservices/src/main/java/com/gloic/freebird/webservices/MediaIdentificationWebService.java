package com.gloic.freebird.webservices;

import com.gloic.freebird.persistence.model.UnknownMedia;
import com.gloic.freebird.services.service.MediaIdentificationService;
import com.gloic.freebird.services.vo.request.EpisodeIdentificationRequest;
import com.gloic.freebird.services.vo.request.MovieIdentificationRequest;
import com.gloic.freebird.services.vo.request.SeasonIdentificationRequest;
import com.gloic.freebird.webservices.response.dto.UnknownMediaDTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gloic
 */
@RestController
@RequestMapping(value = "/api/identify", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MediaIdentificationWebService {

    private final MediaIdentificationService mediaIdentificationService;

    @Autowired
    public MediaIdentificationWebService(MediaIdentificationService mediaIdentificationService) {
        this.mediaIdentificationService = mediaIdentificationService;
    }


    @GetMapping(path = "/p={page}")
    public List<UnknownMediaDTO> findAllUnknown(@PathVariable("page") int page) {
        return GenericMapper.toListDTO(mediaIdentificationService.findAllUnknown(page));
    }

    @PostMapping(path = "/movie")
    public boolean identifyMovie(MovieIdentificationRequest movieIdentificationRequest) {
        return mediaIdentificationService.identifyMovie(movieIdentificationRequest);
    }

    @PostMapping(path = "/episode")
    public boolean identifyEpisode(EpisodeIdentificationRequest episodeIdentificationRequest) {
        return mediaIdentificationService.identifyEpisode(episodeIdentificationRequest);
    }

    @PostMapping(path = "/season")
    public boolean identifySeason(SeasonIdentificationRequest seasonIdentificationRequest) {
        return mediaIdentificationService.identifySeason(seasonIdentificationRequest);
    }

    @PostMapping(path = "/rescan")
    public void rescan(UnknownMedia media) {
        mediaIdentificationService.rescan(media);
    }

    @PutMapping(value = "/wrongMovieIdentification")
    public void wrongMovieIdentification(@RequestParam("movieId") Long movieId, @RequestParam("linkId") Long linkId) {
        mediaIdentificationService.wrongMovieIdentification(movieId, linkId);
    }

    @PutMapping(value = "/wrongEpisodeIdentification")
    public void wrongEpisodeIdentification(@RequestParam("episodeId") Long episodeId, @RequestParam("linkId") Long linkId) {
        mediaIdentificationService.wrongEpisodeIdentification(episodeId, linkId);
    }

    @PutMapping(value = "/ignore/{id}")
    public void ignoreUnknown(@RequestParam("id") Long id) {
        mediaIdentificationService.ignoreMedia(id);
    }

    @PutMapping(value = "/ignoreFolder/{mediaId}")
    public void ignoreFolder(@PathVariable("mediaId") Long id) {
        mediaIdentificationService.ignoreFolder(id);
    }
}
