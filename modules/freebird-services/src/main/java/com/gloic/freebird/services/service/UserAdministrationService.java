package com.gloic.freebird.services.service;


import com.gloic.freebird.commons.enumerations.Authority;
import com.gloic.freebird.persistence.model.User;
import com.gloic.freebird.persistence.model.User_;
import com.gloic.freebird.persistence.repository.UserRepository;
import com.gloic.freebird.services.config.InstallationConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Service dedicated to manage the users.
 * Used by AdministrationPanel
 *
 * @author gloic
 */
@Service
@Slf4j
public class UserAdministrationService {
    private final UserRepository userRepository;

    @Autowired
    public UserAdministrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Create and persist a user from given information
     *
     * @param userName
     * @param password
     * @param authorities
     * @return a userVO
     */
    public User createUser(String userName, String password, Authority... authorities) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(passwordEncoder().encode(password));
        user.setEnabled(true);
        user.setLastPasswordResetDate(new Date());
        Collections.addAll(user.getAuthorities(), authorities);
        userRepository.save(user);
        return user;
    }

    /**
     * If no user is yet registered in the application, a default user will be created.
     * This method a called at server's start in {@link InstallationConfiguration}
     */
    public void createAdminUser() {
        // Create user admin if no user is in the database
        if (userRepository.count() == 0) {
            log.debug("No user registered, creating 'admin'");
            createUser("admin", "admin", Authority.ROLE_USER, Authority.ROLE_ADMIN);
        }
    }

    /**
     * Return all the users
     *
     * @return
     */
    public List<User> getAllUsers() {
        Sort sort = new Sort(Sort.Direction.ASC, User_.id.getName());
        return userRepository.findAll(sort);
    }

    /**
     * Delete a user from its id
     *
     * @param id
     * @return
     */
    public boolean deleteUser(Long id) {
        userRepository.delete(id);
        return true;
    }
}
