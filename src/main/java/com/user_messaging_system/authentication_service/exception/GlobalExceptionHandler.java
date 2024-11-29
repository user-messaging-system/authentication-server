package com.user_messaging_system.authentication_service.exception;

import com.user_messaging_system.core_library.common.annotation.LogExecution;
import com.user_messaging_system.core_library.exception.UserNotFoundException;
import com.user_messaging_system.core_library.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import java.util.List;

import static com.user_messaging_system.core_library.common.constant.ErrorConstant.NO_ROOT_CAUSE_AVAILABLE;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @LogExecution
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception,
        WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse.Builder()
                .message(exception.getMessage())
                .errors(List.of(exception.getCause() != null ? exception.getCause().getMessage() : NO_ROOT_CAUSE_AVAILABLE))
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false))
                .build()
            );
    }

    @LogExecution
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
        UserNotFoundException exception,
        WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse.Builder()
                .message(exception.getMessage())
                .errors(List.of(exception.getCause() != null ? exception.getCause().getMessage() : NO_ROOT_CAUSE_AVAILABLE))
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false))
                .build()
            );
    }

    @LogExecution
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
        AuthenticationException exception,
        WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse.Builder()
                .message(exception.getMessage())
                .errors(List.of(exception.getCause() != null ? exception.getCause().getMessage() : NO_ROOT_CAUSE_AVAILABLE))
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false))
                .build()
            );
    }
}
