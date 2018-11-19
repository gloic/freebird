package com.gloic.freebird.services.scraper.mapper;

import com.gloic.freebird.persistence.model.Season;
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo;
import org.springframework.data.rest.core.util.Function;

/**
 * Map the result of API to a local persistent
 *
 * @author gloic
 */
public class TVSeasonInfoToSeason implements Function<TVSeasonInfo, Season> {
    @Override
    public Season apply(TVSeasonInfo input) {
        Season result = new Season();
        result.setSeasonNumber(input.getSeasonNumber());
        result.setOverview(input.getOverview());
        result.setPosterPath(input.getPosterPath());
        result.setEpisodeCount(input.getEpisodes() != null ? input.getEpisodes().size() : 0);
        result.setAirDate(input.getAirDate());
        return result;
    }
}
