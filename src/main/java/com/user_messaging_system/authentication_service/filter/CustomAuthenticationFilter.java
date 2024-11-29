package com.user_messaging_system.authentication_service.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_messaging_system.authentication_service.api.input.LoginInput;
import com.user_messaging_system.authentication_service.exception.CustomAuthenticationFailureHandler;
import com.user_messaging_system.core_library.response.CustomUserDetails;
import com.user_messaging_system.core_library.response.ErrorResponse;
import com.user_messaging_system.core_library.response.SuccessResponse;
import com.user_messaging_system.core_library.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.user_messaging_system.authentication_service.constant.EndpointConstant.USER_LOGIN_URL;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final Validator validator;
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(USER_LOGIN_URL, "POST");

    public CustomAuthenticationFilter(
        AuthenticationManager authenticationManager,
        JWTService jwtService,
        CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
        Validator validator
    ){
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.validator = validator;
        setAuthenticationFailureHandler(customAuthenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        LoginInput loginInput = obtainLoginInput(request);

         Set<ConstraintViolation<LoginInput>> violations = validator.validate(loginInput);

        if (!violations.isEmpty()) {
            handleValidationErrors(response, violations, request.getRequestURI());
            return null;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginInput.email(), loginInput.password());
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String accessToken = generateAccessToken(userDetails);
        sendSuccessResponse(response, accessToken);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        this.getFailureHandler().onAuthenticationFailure(request, response, failed);
    }

    private LoginInput obtainLoginInput(HttpServletRequest request) throws IOException {
        return new ObjectMapper().readValue(request.getInputStream(), LoginInput.class);
    }

    private void sendSuccessResponse(HttpServletResponse response, String accessToken) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());

        Map<String, String> data = new HashMap<>();
        data.put("accessToken", accessToken);

        SuccessResponse<Map<String, String>> responseData = new SuccessResponse.Builder<Map<String, String>>()
                .message("Login successful")
                .data(data)
                .status(String.valueOf(HttpStatus.OK))
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), responseData);
    }

    private String generateAccessToken(CustomUserDetails userDetails) {
        return jwtService.generateJwtToken(
            userDetails.getUsername(),
            userDetails.getId(),
            userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }


    private void handleValidationErrors(
            HttpServletResponse response,
            Set<ConstraintViolation<LoginInput>> violations,
            String path
    ) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        List<String> errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .message("Input validation error. Please check the provided data.")
                .errors(errorMessages)
                .status(HttpStatus.BAD_REQUEST.value())
                .path(path)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
