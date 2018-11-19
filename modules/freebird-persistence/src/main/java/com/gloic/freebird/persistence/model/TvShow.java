package com.gloic.freebird.persistence.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gloic
 */
@Entity
@Table(
        name = "TV_SHOW",
        uniqueConstraints = @UniqueConstraint(columnNames = {"idShow"})
)
@Data
public class TvShow {
    @Id
    @GeneratedValue
    private Long id;

    private Integer idShow;

    private String title;

    @Type(type = "text")
    private String overview;

    @OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Season> seasons = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "aliases", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "aliases")
    private List<String> aliases = new ArrayList<>();

    private String posterPath;

    private LocalDateTime updated;

    private String firstAirDate;

    private float popularity;

    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Genre> genres = new ArrayList<>();

    public void addSeason(Season season) {
        this.seasons.add(season);
    }

}
