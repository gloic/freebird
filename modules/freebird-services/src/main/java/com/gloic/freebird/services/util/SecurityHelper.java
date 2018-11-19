package com.gloic.freebird.services.util;

import com.gloic.freebird.persistence.model.JwtUser;
import com.gloic.freebird.persistence.model.UserPreferences;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Helper that permits to retrieve data from the current logged used
 *
 * @author gloic
 */
public class SecurityHelper {

    public static JwtUser getCurrentJwtUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (JwtUser) authentication.getPrincipal();
        }
        return null;
    }

    public static UserPreferences getUserPreferences() {
        JwtUser currentJwtUser = getCurrentJwtUser();
        if (currentJwtUser != null) {
            return currentJwtUser.getUserPreferences();
        }
        return null;
    }

}
