package com.unimag.unimagleisureinventory.dtos.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateStudentRequestDTO(
        @NotNull(message = "Student id cant be null") Long studentId,
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        // TODO: la extension del email podria pasarse a una variable de entorno
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@unimagdalena\\.edu\\.co$",
                message = "Must be an institutional email @unimagdalena.edu.co"
        )String email,
        @NotBlank(message = "Password is required") String password
) {
}
