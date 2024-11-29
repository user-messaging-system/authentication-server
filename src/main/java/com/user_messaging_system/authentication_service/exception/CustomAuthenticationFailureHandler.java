package com.user_messaging_system.authentication_service.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_messaging_system.core_library.exception.UserNotFoundException;
import com.user_messaging_system.core_library.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        String errorMessage = "Authentication failed";
        int status = HttpStatus.UNAUTHORIZED.value();

        if (exception instanceof UsernameNotFoundException) {
            Throwable cause = exception.getCause();
            if (cause instanceof UserNotFoundException) {
                errorMessage = cause.getMessage();
                status = HttpStatus.NOT_FOUND.value();
            }
        }

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .message(errorMessage)
                .errors(List.of("Authentication Failed"))
                .status(status)
                .path(request.getRequestURI())
                .build();

        response.setStatus(status);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
