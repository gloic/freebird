package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.Season;
import com.gloic.freebird.webservices.response.mapper.DTO;
import lombok.Data;

import java.io.Serializable;

/**
 * SeasonLightVO is dedicated to be displayed in a grid or a list and contains minimal data
 *
 * @author gloic
 */
@DTO(DtoOf = Season.class, light = true)
@Data
public class SeasonLightDTO implements Serializable {

    private Long id;
    private Integer seasonNumber;
    private String posterPath;
    private int episodeCount;
    private int episodeCountInDB;
    private String airDate;

    public SeasonLightDTO(Season se) {
        this.id = se.getId();
        this.seasonNumber = se.getSeasonNumber();
        this.posterPath = se.getPosterPath();
        this.episodeCount = se.getEpisodeCount();
        this.episodeCountInDB = se.getEpisodes().size();
        this.airDate = se.getAirDate();
    }
}
