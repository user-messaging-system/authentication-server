package com.user_messaging_system.authentication_service.service;

import com.user_messaging_system.authentication_service.api.output.UserOutput;
import com.user_messaging_system.authentication_service.client.UserClient;
import com.user_messaging_system.core_library.exception.UserNotFoundException;
import com.user_messaging_system.core_library.response.CustomUserDetails;
import com.user_messaging_system.core_library.response.SuccessResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserClient userClient;

    public CustomUserDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    @Retry(name = "load-user-retry", fallbackMethod = "getByEmail_Fallback")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<SuccessResponse<UserOutput>> userOutputResponseEntity = userClient.getByEmail(username);
        UserOutput userOutput = validateUserOutput(userOutputResponseEntity);
        return new CustomUserDetails(
                userOutput.id(),
                userOutput.email(),
                userOutput.password(),
                userOutput.roles().stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    private UserOutput validateUserOutput(ResponseEntity<SuccessResponse<UserOutput>> userOutputResponseEntity){
        return Optional.ofNullable(userOutputResponseEntity)
                .map(ResponseEntity::getBody)
                .map(SuccessResponse::getData)
                .orElseThrow(() -> new UserNotFoundException("User not found or data is null"));
    }

    private List<GrantedAuthority> getGrantedAuthority(List<String> roles){
        return roles.stream().map(role -> (GrantedAuthority) () -> role).toList();
    }
}
