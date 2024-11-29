package com.user_messaging_system.authentication_service.security;

import com.user_messaging_system.authentication_service.exception.CustomAuthenticationFailureHandler;
import com.user_messaging_system.authentication_service.filter.CustomAuthenticationFilter;
import com.user_messaging_system.core_library.service.JWTService;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final JWTService jwtService;
    private final Validator validator;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider, JWTService jwtService, Validator validator) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.jwtService = jwtService;
        this.validator = validator;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationConfiguration authenticationConfiguration,
            CustomAuthenticationFailureHandler customAuthenticationFailureHandler) throws Exception {
         http.csrf(AbstractHttpConfigurer::disable)
                 .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(new CustomAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtService, customAuthenticationFailureHandler, validator), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authenticationProvider(customAuthenticationProvider);

        return http.build();
    }
}
