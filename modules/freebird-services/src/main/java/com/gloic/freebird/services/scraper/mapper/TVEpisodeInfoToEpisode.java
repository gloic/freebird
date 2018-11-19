package com.gloic.freebird.services.scraper.mapper;

import com.gloic.freebird.persistence.model.Episode;
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo;
import org.springframework.data.rest.core.util.Function;

/**
 * Map the result of API to a local persistent
 *
 * @author gloic
 */
public class TVEpisodeInfoToEpisode implements Function<TVEpisodeInfo, Episode> {
    @Override
    public Episode apply(TVEpisodeInfo input) {
        Episode result = new Episode();
        result.setEpisodeNum(input.getEpisodeNumber());
        result.setTitle(input.getName());
        result.setStillPath(input.getStillPath());
        result.setAirDate(input.getAirDate());
        result.setOverview(input.getOverview());

        return result;
    }
}
