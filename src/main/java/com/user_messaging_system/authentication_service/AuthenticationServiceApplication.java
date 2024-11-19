package com.user_messaging_system.authentication_service;

import com.user_messaging_system.authentication_service.client.UserErrorDecoder;
import com.user_messaging_system.core_library.service.JWTService;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableFeignClients
@Import(JWTService.class)
public class AuthenticationServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

    @Bean
    public ErrorDecoder errorDecoder(){
        return new UserErrorDecoder();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
