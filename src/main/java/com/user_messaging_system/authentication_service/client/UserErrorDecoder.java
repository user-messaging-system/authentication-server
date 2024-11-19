package com.user_messaging_system.authentication_service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_messaging_system.core_library.exception.UserNotFoundException;
import com.user_messaging_system.core_library.response.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserErrorDecoder implements ErrorDecoder {
    private static final Logger logger = LoggerFactory.getLogger(UserErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorResponse errorResponse = null;

        if(Objects.nonNull(response.body())){
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().asInputStream()))){
                String body = bufferedReader.lines().collect(Collectors.joining("\n"));
                errorResponse = objectMapper.readValue(body, ErrorResponse.class);
            }catch (IOException e){
                logger.error("Error decoding response body for method {}: {}", methodKey, e.getMessage());
                return new Exception("Error processing response body", e);
            }
        }else{
            logger.warn("Response body is null for method {}", methodKey);
        }

        return determineExceptionType(errorResponse, methodKey, response);
    }

    private Exception determineExceptionType(ErrorResponse errorResponse,String methodKey, Response response){
        if (Objects.nonNull(errorResponse)) {
            if (response.status() == 404) {
                return new UserNotFoundException(errorResponse.getMessage());
            }
        }else{
            logger.warn("ErrorResponse is null for status {} and method {}", response.status(), methodKey);
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
