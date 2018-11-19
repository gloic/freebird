package com.gloic.freebird.services.wrapper;

import com.gloic.freebird.persistence.model.Season;
import com.gloic.freebird.persistence.model.TvShow;
import lombok.Data;

import java.util.List;

@Data
public class TvShowWrapper {
    private TvShow tvShow;
    private List<Season> seasons;

    public TvShowWrapper(TvShow tvShow, List<Season> seasons) {
        this.tvShow = tvShow;
        this.seasons = seasons;
    }
}
