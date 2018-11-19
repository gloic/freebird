package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.UnknownMedia;
import com.gloic.freebird.webservices.response.mapper.DTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.Data;

@DTO(DtoOf = UnknownMedia.class, light = true)
@Data
public class UnknownMediaLightDTO {
    private Long id;
    protected LinkLightDTO link;

    public UnknownMediaLightDTO(UnknownMedia unknownMedia) {
        this.id = unknownMedia.getId();
        this.link = GenericMapper.toLightDTO(unknownMedia.getLinks().iterator().next());
    }
}
