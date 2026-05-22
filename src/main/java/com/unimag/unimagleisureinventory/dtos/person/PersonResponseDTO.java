package com.unimag.unimagleisureinventory.dtos.person;

import com.unimag.unimagleisureinventory.model.enums.Role;

import java.util.UUID;

public record PersonResponseDTO(
        UUID personId,
        String firstName,
        String lastName,
        String email,
        Role role
) {
}
