package com.gloic.freebird.services.scraper.mapper;

import com.gloic.freebird.persistence.model.TvShow;
import com.omertron.themoviedbapi.model.tv.TVBasic;
import org.springframework.data.rest.core.util.Function;

/**
 * Map the result of API to a local persistent
 *
 * @author gloic
 */
public class TVBasicToTVShow implements Function<TVBasic, TvShow> {

    @Override
    public TvShow apply(TVBasic input) {
        TvShow result = new TvShow();
        result.setIdShow(input.getId());
        result.setTitle(input.getName());
        result.setPosterPath(input.getPosterPath());
        result.setOverview(input.getOverview());
        result.setFirstAirDate(input.getFirstAirDate());
        result.setPopularity(input.getPopularity());
        return result;
    }
}
