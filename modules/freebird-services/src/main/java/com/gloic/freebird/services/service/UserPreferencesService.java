package com.gloic.freebird.services.service;


import com.gloic.freebird.persistence.model.JwtUser;
import com.gloic.freebird.persistence.model.User;
import com.gloic.freebird.persistence.model.UserPreferences;
import com.gloic.freebird.persistence.repository.UserPreferencesRepository;
import com.gloic.freebird.persistence.repository.UserRepository;
import com.gloic.freebird.services.util.SecurityHelper;
import com.gloic.freebird.services.vo.request.ChangePasswordRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Retrieve and update the UserPreferences of the currently logged user
 *
 * @author gloic
 */
@Service
@Slf4j
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserPreferencesService(UserPreferencesRepository userPreferencesRepository, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userPreferencesRepository = userPreferencesRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    /**
     * @return the preferences of the logged user
     */
    public UserPreferences getUserPreferences() {
        return SecurityHelper.getUserPreferences();
    }

    public UserPreferences update(UserPreferences userPreferences) {
        UserPreferences preferences = SecurityHelper.getUserPreferences();
        preferences.setLanguagesMovies(userPreferences.getLanguagesMovies());
        preferences.setLanguagesTvShows(userPreferences.getLanguagesTvShows());
        preferences.setQualities(userPreferences.getQualities());
        userPreferencesRepository.save(preferences);
        return preferences;
    }

    public boolean changePassword(ChangePasswordRequest changePasswordRequest) {
        JwtUser currentJwtUser = SecurityHelper.getCurrentJwtUser();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            currentJwtUser.getUsername(),
                            changePasswordRequest.getCurrentPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.error("A BadCredentialsException occurred when user '{}' tried to change its password.", currentJwtUser.getUsername());
            return false;
        }


        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = userRepository.findOne(currentJwtUser.getId());
        user.setPassword(bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setLastPasswordResetDate(new Date());
        userRepository.save(user);
        return true;
    }
}