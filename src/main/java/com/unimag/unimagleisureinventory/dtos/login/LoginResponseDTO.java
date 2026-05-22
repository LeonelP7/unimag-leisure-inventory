package com.unimag.unimagleisureinventory.dtos.login;

import java.util.UUID;

public record LoginResponseDTO(
        String token,
        String role,
        UUID personId
) {
}
