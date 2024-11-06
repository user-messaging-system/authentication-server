package com.user_messaging_system.authentication_service.api.response;

import java.time.LocalDateTime;

public record FallbackResponse(
        String service,
        String message,
        String errorCode,
        LocalDateTime timestamp
){}
