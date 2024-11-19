package com.user_messaging_system.authentication_service.security;

import com.user_messaging_system.authentication_service.service.CustomUserDetailsService;
import com.user_messaging_system.core_library.exception.UserNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;

    public CustomAuthenticationProvider(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authentication.getName());
            return validatePassword((String) authentication.getCredentials(), userDetails.getPassword(), userDetails);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Authentication validatePassword(String requestedPassword, String dbPassword, UserDetails userDetails) {
        if (requestedPassword.equals(dbPassword)) {
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }else{
            throw new UserNotFoundException("UserNotFoundException");
        }
    }
}
