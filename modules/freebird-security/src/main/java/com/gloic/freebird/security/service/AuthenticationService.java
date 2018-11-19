package com.gloic.freebird.security.service;


import com.gloic.freebird.persistence.model.JwtUser;
import com.gloic.freebird.security.util.JwtTokenUtil;
import com.gloic.freebird.security.webservice.vo.JwtAuthenticationRequest;
import com.gloic.freebird.security.webservice.vo.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gloic
 */
@Service
public class AuthenticationService {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Generate a response with the token and user's authorities if the user can be logged
     *
     * @param authenticationRequest
     * @return
     */
    public JwtAuthenticationResponse createAuthenticationToken(JwtAuthenticationRequest authenticationRequest) {
        // Perform the model
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-model so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateClaims(userDetails);

        // Return the token
        return new JwtAuthenticationResponse(token, jwtTokenUtil.mapToAuthorities(userDetails.getAuthorities()));
    }

    /**
     * Refresh the token
     *
     * @param request
     * @return
     */
    public JwtAuthenticationResponse refreshAndGetAuthToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return new JwtAuthenticationResponse(refreshedToken, jwtTokenUtil.mapToAuthorities(user.getAuthorities()));
        }

        return null;
    }
}
