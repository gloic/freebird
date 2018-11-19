package com.gloic.freebird.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gloic
 */
@Entity
@Data
@EqualsAndHashCode(exclude = "tvShow")
@ToString(exclude = "tvShow")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer seasonNumber;

    @Type(type = "text")
    private String overview;

    private String posterPath;

    private Integer episodeCount;

    private String airDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tv_show_id", referencedColumnName = "id")
    private TvShow tvShow;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Episode> episodes = new ArrayList<>();

    public void addEpisode(Episode episode) {
        this.episodes.add(episode);
    }
}
