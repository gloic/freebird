package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.persistence.model.UnknownMedia;
import com.gloic.freebird.webservices.response.mapper.DTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;

@DTO(DtoOf = UnknownMedia.class)
public class UnknownMediaDTO extends UnknownMediaLightDTO {

    public UnknownMediaDTO(UnknownMedia unknownMedia) {
        super(unknownMedia);
        this.link = GenericMapper.toDTO(unknownMedia.getLinks().iterator().next());
    }
}