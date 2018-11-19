package com.gloic.freebird.webservices;

import com.gloic.freebird.commons.enumerations.Authority;
import com.gloic.freebird.persistence.model.User;
import com.gloic.freebird.services.service.UserAdministrationService;
import com.gloic.freebird.webservices.response.dto.UserDTO;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gloic
 */
@RestController
@RequestMapping(value = "/api/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAdministrationWebService {

    private final UserAdministrationService userAdministrationService;

    @Autowired
    public UserAdministrationWebService(UserAdministrationService userAdministrationService) {
        this.userAdministrationService = userAdministrationService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/")
    public List<UserDTO> findAllUser() {
        return GenericMapper.toListDTO(userAdministrationService.getAllUsers());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create")
    public UserDTO autoCreateNewUser(@RequestBody User user) {
        return GenericMapper.toDTO(userAdministrationService.createUser(user.getUsername(), user.getPassword(), Authority.ROLE_USER));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/delete")
    public boolean deleteUser(@RequestParam("userId") Long id) {
        return userAdministrationService.deleteUser(id);
    }

}
