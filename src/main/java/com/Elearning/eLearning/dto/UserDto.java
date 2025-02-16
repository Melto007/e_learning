package com.Elearning.eLearning.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "password is required")
        String password,

        @NotBlank(message = "email is required")
        @Email(message = "Invalid email address")
        String email
) {
}
