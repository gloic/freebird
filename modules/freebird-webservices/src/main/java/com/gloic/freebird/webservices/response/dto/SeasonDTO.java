package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.Season;
import com.gloic.freebird.webservices.response.mapper.DTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

/**
 * SeasonDetailVO inherits from SeasonLightVO and add the episodes and TVShow
 *
 * @author gloic
 */
@DTO(DtoOf = Season.class)
@Data
public final class SeasonDTO extends SeasonLightDTO {

    private String overview;
    private TvShowLightDTO tvShow;
    private List<EpisodeDTO> episodes;

    public SeasonDTO(Season se) {
        super(se);
        this.overview = se.getOverview();
        this.tvShow = GenericMapper.toLightDTO(se.getTvShow());
        List<EpisodeDTO> collect = GenericMapper.toListDTO(se.getEpisodes());
        collect.sort(Comparator.comparing(EpisodeDTO::getEpisodeNum));
        this.episodes = collect;
    }
}
