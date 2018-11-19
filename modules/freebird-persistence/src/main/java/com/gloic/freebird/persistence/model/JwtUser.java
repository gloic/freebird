package com.gloic.freebird.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * <p>This class is not a persistent but a local pojo used by the security in order to identify a logged user.</p>
 * <p>The class is present here because the modules that use it have this module in dependencies</p>
 *
 * @author gloic
 */
@Data
public class JwtUser implements UserDetails {

    @JsonIgnore
    private final Long id;

    private final String username;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private final boolean enabled;

    @JsonIgnore
    private final Date lastPasswordResetDate;

    @JsonIgnore
    private final UserPreferences userPreferences;

    public JwtUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled, Date lastPasswordResetDate, UserPreferences userPreferences) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.userPreferences = userPreferences;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
