package com.gloic.freebird.persistence.repository;

import com.gloic.freebird.persistence.model.Season;
import com.gloic.freebird.persistence.model.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author gloic
 */
public interface SeasonRepository extends JpaRepository<Season, Long>, JpaSpecificationExecutor<Season> {

    Season findByTvShowAndSeasonNumber(TvShow tvShow, int seasonNum);
}
