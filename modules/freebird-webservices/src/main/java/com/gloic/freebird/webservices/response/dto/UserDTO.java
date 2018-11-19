package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.commons.enumerations.Authority;
import com.gloic.freebird.persistence.model.User;
import com.gloic.freebird.webservices.response.mapper.DTO;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * UserVO is used in the administration panel
 *
 * @author gloic
 */
@DTO(DtoOf = User.class)
@Data
public class UserDTO {
    private Long id;
    private String username;
    private boolean enabled;
    private Date lastPasswordResetDate;
    private Set<Authority> authorities;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.lastPasswordResetDate = user.getLastPasswordResetDate();
        this.authorities = user.getAuthorities();
    }
}
