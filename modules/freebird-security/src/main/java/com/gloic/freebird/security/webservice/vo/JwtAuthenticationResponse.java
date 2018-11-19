package com.gloic.freebird.security.webservice.vo;

import com.gloic.freebird.commons.enumerations.Authority;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * Response object after a successful login.
 * It contains the jwt token and the user's authorities
 *
 * @author gloic
 */
@Data
public class JwtAuthenticationResponse implements Serializable {

    private final String token;
    private final Collection<Authority> authorities;

    public JwtAuthenticationResponse(String token, Collection<Authority> authorities) {
        this.token = token;
        this.authorities = authorities;
    }
}
