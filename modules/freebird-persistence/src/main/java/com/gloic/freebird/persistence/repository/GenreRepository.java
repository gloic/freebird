package com.gloic.freebird.persistence.repository;

import com.gloic.freebird.persistence.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gloic
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
