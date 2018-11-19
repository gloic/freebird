package com.gloic.freebird.persistence.repository;


import com.gloic.freebird.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gloic
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String userName);

}
