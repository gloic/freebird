package com.gloic.freebird.persistence.repository;

import com.gloic.freebird.persistence.model.Genre;
import com.gloic.freebird.persistence.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author gloic
 */
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    Movie findByTitle(String title);

    @Query("SELECT DISTINCT g FROM Movie m JOIN m.genres g ORDER BY g.name ASC")
    List<Genre> findAllGenres();
}
