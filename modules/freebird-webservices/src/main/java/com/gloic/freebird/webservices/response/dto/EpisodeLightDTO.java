package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.webservices.response.mapper.DTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * EpisodeLightVO contains minimal information to be displayed in a grid or a list
 *
 * @author gloic
 */
@DTO(DtoOf = Episode.class, light = true)
@Data
public class EpisodeLightDTO implements Serializable {
    protected Long id;
    protected String url;
    protected Integer episodeNum;
    protected String title;
    protected String stillPath;
    protected List<? extends LinkLightDTO> links;

    public EpisodeLightDTO(Episode e) {
        this.id = e.getId();
        this.title = e.getTitle();
        this.episodeNum = e.getEpisodeNum();
        this.stillPath = e.getStillPath();
        this.links = GenericMapper.toListLightDTO(e.getLinks());
    }
}
