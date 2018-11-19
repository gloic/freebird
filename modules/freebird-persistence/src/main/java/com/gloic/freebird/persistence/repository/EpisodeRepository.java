package com.gloic.freebird.persistence.repository;

import com.gloic.freebird.persistence.model.Episode;
import com.gloic.freebird.persistence.model.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author gloic
 */
public interface EpisodeRepository extends JpaRepository<Episode, Long>, JpaSpecificationExecutor<Episode> {

    Episode findByEpisodeNumAndSeasonSeasonNumberAndSeasonTvShow(int episodeNum, int seasonNum, TvShow tvShow);

}
