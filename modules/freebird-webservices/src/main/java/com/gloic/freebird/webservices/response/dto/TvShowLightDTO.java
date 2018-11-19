package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.TvShow;
import com.gloic.freebird.webservices.response.mapper.DTO;
import lombok.Data;

import java.io.Serializable;

@DTO(DtoOf = TvShow.class, light = true)
@Data
public class TvShowLightDTO implements Serializable {

    private Long id;
    private String title;
    private String posterPath;

    public TvShowLightDTO(TvShow tvShow) {
        id = tvShow.getId();
        title = tvShow.getTitle();
        posterPath = tvShow.getPosterPath();
    }

}
