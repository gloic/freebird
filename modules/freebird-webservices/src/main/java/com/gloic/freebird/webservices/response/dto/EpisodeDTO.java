package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.webservices.response.mapper.DTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.Data;

/**
 * EpisodeDetailVO inherits from EpisodeLightVO and add detailed informations about the episode
 *
 * @author gloic
 */
@DTO(DtoOf = Episode.class)
@Data
public final class EpisodeDTO extends EpisodeLightDTO {

    private Long size;
    private String overview;
    private String airDate;
    private SeasonLightDTO season;
    private TvShowLightDTO tvShow;

    public EpisodeDTO(Episode e) {
        super(e);

        this.overview = e.getOverview();
        this.airDate = e.getAirDate();

        this.season = GenericMapper.toLightDTO(e.getSeason());
        this.tvShow = GenericMapper.toLightDTO(e.getSeason().getTvShow());
        this.links = GenericMapper.toListDTO(e.getLinks());
    }
}