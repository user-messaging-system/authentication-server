package com.user_messaging_system.authentication_service.client;

import com.user_messaging_system.authentication_service.api.output.UserOutput;
import com.user_messaging_system.authentication_service.config.FeignConfig;
import com.user_messaging_system.core_library.response.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserClient {
    @GetMapping("/v1/api/users/by-email")
    ResponseEntity<SuccessResponse<UserOutput>> getByEmail(@RequestParam("email") String email);
}
