package com.unimag.unimagleisureinventory.dtos.person;

import com.unimag.unimagleisureinventory.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePersonRequestDTO(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "The password is required") String password,
        @NotNull(message = "Role is required") Role role
) {
}
