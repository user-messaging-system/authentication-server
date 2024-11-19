package com.user_messaging_system.authentication_service.exception;

import com.user_messaging_system.core_library.exception.UserNotFoundException;
import com.user_messaging_system.core_library.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
        UserNotFoundException exception,
        WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse.Builder()
                .message(exception.getMessage())
                .error(exception.getCause() != null ? exception.getCause().getMessage() : "No root cause available")
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false))
                .build()
            );
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
        AuthenticationException exception,
        WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse.Builder()
                .message(exception.getMessage())
                .error(exception.getCause() != null ? exception.getCause().getMessage() : "No root cause available")
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false))
                .build()
            );
    }

}
