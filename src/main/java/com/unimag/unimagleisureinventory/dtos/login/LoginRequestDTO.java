package com.unimag.unimagleisureinventory.dtos.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "The email is required")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "The password is required")
        String password
) {
    public String getEmail() {
    }
}
