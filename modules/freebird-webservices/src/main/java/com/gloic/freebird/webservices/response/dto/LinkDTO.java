package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.commons.enumerations.Codec;
import com.gloic.freebird.commons.enumerations.Language;
import com.gloic.freebird.commons.enumerations.Quality;
import com.gloic.freebird.persistence.model.Link;
import com.gloic.freebird.webservices.response.mapper.DTO;
import lombok.Data;

/**
 * Extends LinkLightVO and add all necessary information to display a link and its metadata
 *
 * @author gloic
 */
@DTO(DtoOf = Link.class)
@Data
public final class LinkDTO extends LinkLightDTO {

    private Long id;
    private String fileName;
    private String parentUrl;
    private Long size;
    private Codec codec;
    private Language language;
    private Quality quality;

    public LinkDTO(Link link) {
        super(link);
        this.id = link.getId();
        this.fileName = link.getFileName();
        this.parentUrl = link.getParentUrl();
        this.size = link.getSize();
        this.codec = link.getCodec();
        this.language = link.getLanguage();
        this.quality = link.getQuality();

    }
}
