package com.gloic.freebird.webservices;


import com.gloic.freebird.persistence.model.UserPreferences;
import com.gloic.freebird.services.service.UserPreferencesService;
import com.gloic.freebird.services.vo.request.ChangePasswordRequest;
import com.gloic.freebird.webservices.response.dto.UserPreferencesDTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author gloic
 */
@RestController
@RequestMapping(value = "/api/preferences", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserPreferencesWebService {

    private final UserPreferencesService userPreferencesService;

    @Autowired
    public UserPreferencesWebService(UserPreferencesService userPreferencesService) {
        this.userPreferencesService = userPreferencesService;
    }

    @GetMapping(value = "/")
    public UserPreferencesDTO getUserPreferences() throws IOException {
        return GenericMapper.toDTO(userPreferencesService.getUserPreferences());
    }

    @PostMapping(value = "/")
    public UserPreferencesDTO save(@RequestBody UserPreferences userPreferences) throws IOException {
        return GenericMapper.toDTO(userPreferencesService.update(userPreferences));
    }

    @PostMapping(value = "/changePassword")
    public boolean changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) throws IOException {
        return this.userPreferencesService.changePassword(changePasswordRequest);
    }
}
