package com.gloic.freebird.persistence.repository;

import com.gloic.freebird.persistence.model.Genre;
import com.gloic.freebird.persistence.model.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author gloic
 */
public interface TvShowRepository extends JpaRepository<TvShow, Long>, JpaSpecificationExecutor<TvShow> {

    TvShow findByIdShow(Integer idShow);

    TvShow findByAliasesIn(String alias);

    @Query("SELECT DISTINCT g FROM TvShow t JOIN t.genres g ORDER BY g.name ASC")
    List<Genre> findAllGenres();
}
