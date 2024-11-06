package com.user_messaging_system.authentication_service.service;

import com.user_messaging_system.authentication_service.api.output.UserOutput;
import com.user_messaging_system.authentication_service.client.UserClient;
import com.user_messaging_system.core_library.response.SuccessResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        UserOutput userOutput = checkUserOutput(userOutputResponseEntity);
        return new User(userOutput.getEmail(), userOutput.getPassword(), getGrantedAuthority(userOutput.getRoles()));
    }

    private UserOutput checkUserOutput(ResponseEntity<SuccessResponse<UserOutput>> userOutputResponseEntity){
        if(Objects.nonNull(userOutputResponseEntity) && Objects.nonNull(userOutputResponseEntity.getBody())){
            return (UserOutput)userOutputResponseEntity.getBody().getData();
        }
        throw new UsernameNotFoundException("User not found");
    }

    private List<GrantedAuthority> getGrantedAuthority(List<String> roles){
        return roles.stream().map(role -> (GrantedAuthority) () -> role).collect(Collectors.toList());
    }
}
