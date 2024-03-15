package org.maxym.spring.sensor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRequestDto(

        @NotEmpty(message = "Username should not be empty")
        @Size(min = 3, max = 30, message = "Username should be between 3 and 30 characters")
        String username,

        @NotEmpty(message = "Password should not be empty")
        @Size(min = 3, max = 30, message = "Password should be between 3 and 30 characters")
        String password,

        @NotEmpty(message = "Email should not be empty")
        @Email(regexp = ".+@(gmail.com|.+.ua)")
        String email
) {
}
