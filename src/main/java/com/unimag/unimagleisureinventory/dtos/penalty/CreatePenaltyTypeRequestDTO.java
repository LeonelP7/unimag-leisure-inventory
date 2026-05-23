package com.unimag.unimagleisureinventory.dtos.penalty;

import jakarta.validation.constraints.NotBlank;

public record CreatePenaltyTypeRequestDTO(
        @NotBlank(message = "Name is required") String name
) {
}
