package com.gloic.freebird.security;

import com.gloic.freebird.persistence.model.User;
import com.gloic.freebird.persistence.repository.UserRepository;
import com.gloic.freebird.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailService for JWTUser
 *
 * @author gloic
 */
@Service
@SuppressWarnings("unused")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtUserDetailsServiceImpl(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Check if the given user exist in database.
     * It's NOT the login part, just the JWTUser creation for current logged user
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return jwtTokenUtil.create(user);
        }
    }
}
