package com.gloic.freebird.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gloic.freebird.commons.enumerations.Codec;
import com.gloic.freebird.commons.enumerations.FileStatus;
import com.gloic.freebird.commons.enumerations.Language;
import com.gloic.freebird.commons.enumerations.MediaCategory;
import com.gloic.freebird.commons.enumerations.Quality;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author gloic
 */
@Entity
@Data
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Type(type = "text")
    private String url;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private MediaCategory category;

    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus = FileStatus.NEW;

    private String parentUrl;

    @OneToOne
    @JsonIgnore
    private Site site;

    private Long size;

    private Codec codec;
    private Language language;
    private Quality quality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id")
    private AbstractMedia media;

    private AbstractMedia getMedia() {
        return media;
    }
}
