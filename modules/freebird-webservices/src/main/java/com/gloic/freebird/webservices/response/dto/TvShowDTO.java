package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.services.wrapper.TvShowWrapper;
import com.gloic.freebird.webservices.response.mapper.DTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.Data;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@DTO(DtoOf = TvShowWrapper.class)
@Data
public class TvShowDTO extends TvShowLightDTO {
    private String overview;
    private Collection<SeasonLightDTO> seasons;
    private String firstAirDate;

    public TvShowDTO(TvShowWrapper tvShowWrapper) {
        super(tvShowWrapper.getTvShow());
        this.overview = tvShowWrapper.getTvShow().getOverview();
        this.firstAirDate = tvShowWrapper.getTvShow().getFirstAirDate();
        List<SeasonLightDTO> collect = GenericMapper.toListLightDTO(tvShowWrapper.getSeasons());
        collect.sort(Comparator.comparing(SeasonLightDTO::getSeasonNumber));
        this.seasons = collect;
    }
}
