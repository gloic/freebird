package com.gloic.freebird.persistence.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * @author gloic
 */
@Entity
@Data
public class Movie extends AbstractMedia {

    private String title;

    @Type(type = "text")
    private String overview;

    private String releaseDate;

    private Integer idMovie;

    private String posterPath;

    private float popularity;

    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Genre> genres;


}
