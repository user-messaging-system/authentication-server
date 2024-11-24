package com.user_messaging_system.authentication_service.config;

import com.user_messaging_system.authentication_service.client.UserErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public UserErrorDecoder userErrorDecoder(){
        return new UserErrorDecoder();
    }
}
