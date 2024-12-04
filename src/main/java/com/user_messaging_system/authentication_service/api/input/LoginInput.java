package com.user_messaging_system.authentication_service.api.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import static com.user_messaging_system.core_library.common.constant.ValidationConstant.*;

@Validated
public record LoginInput(
        @NotBlank(message = EMAIL_NOT_BLANK)
        @Email(message = INVALID_EMAIL)
        @Size(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH, message = INVALID_EMAIL)
        String email,

        @NotBlank(message = PASSWORD_NOT_BLANK)
        @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = INVALID_PASSWORD)
        String password
)
{}
