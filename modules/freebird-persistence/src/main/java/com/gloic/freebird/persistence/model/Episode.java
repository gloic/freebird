package com.gloic.freebird.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author gloic
 */
@Entity
@Data
@EqualsAndHashCode(exclude = "season")
@ToString(exclude = "season")
public class Episode extends AbstractMedia {

    private String title;

    private int episodeNum;

    private String stillPath;

    @Type(type = "text")
    private String overview;

    private String airDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id", referencedColumnName = "id")
    private Season season;

}
