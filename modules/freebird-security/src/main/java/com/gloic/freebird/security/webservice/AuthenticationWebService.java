package com.gloic.freebird.security.webservice;

import com.gloic.freebird.security.service.AuthenticationService;
import com.gloic.freebird.security.webservice.vo.JwtAuthenticationRequest;
import com.gloic.freebird.security.webservice.vo.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Webservice for user's authentication
 *
 * @author gloic
 */
@RestController
@RequestMapping(value = "/auth/", produces = MediaType.APPLICATION_JSON_VALUE)
@SuppressWarnings("unused")
public class AuthenticationWebService {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationWebService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "authenticate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JwtAuthenticationResponse createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        return authenticationService.createAuthenticationToken(authenticationRequest);
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JwtAuthenticationResponse refreshAndGetAuthenticationToken(HttpServletRequest request) {
        return authenticationService.refreshAndGetAuthToken(request);
    }


}
