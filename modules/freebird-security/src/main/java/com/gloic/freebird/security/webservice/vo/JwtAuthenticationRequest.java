package com.gloic.freebird.security.webservice.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Request for a login.
 *
 * @author gloic
 */
@Data
public class JwtAuthenticationRequest implements Serializable {

    private String username;
    private String password;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
