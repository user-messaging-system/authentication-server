package com.user_messaging_system.authentication_service.fallback;

import com.user_messaging_system.authentication_service.api.response.FallbackResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserServiceFallback {
    public ResponseEntity<FallbackResponse> getByEmailFallback(){
        FallbackResponse fallbackResponse = new FallbackResponse(
                "user-service",
                "User Service is currently unavailable/ Bu mesaj getByEmail_Fallback tarafindan olusturuldu.",
                String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse);
    }
}
