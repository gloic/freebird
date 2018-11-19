package com.gloic.freebird.persistence.repository;


import com.gloic.freebird.persistence.model.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gloic
 */
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
}
