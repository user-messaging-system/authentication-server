package com.user_messaging_system.authentication_service.api.output;

import java.util.List;

public record UserOutput(
        String id,
        String email,
        String name,
        String lastName,
        String password,
        List<String> roles
){}