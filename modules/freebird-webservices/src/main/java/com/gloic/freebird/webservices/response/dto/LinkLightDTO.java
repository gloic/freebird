package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.webservices.response.mapper.DTO;
import lombok.Data;

/**
 * LinkLightVO contains minimal information
 *
 * @author gloic
 */
@DTO(DtoOf = Link.class, light = true)
@Data
public class LinkLightDTO {

    private String url;

    public LinkLightDTO(Link link) {
        this.url = link.getUrl();
    }
}
