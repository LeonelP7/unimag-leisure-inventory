package com.unimag.unimagleisureinventory.dtos;

import jakarta.validation.constraints.NotBlank;

public record ResolvePenaltyRequestDTO(
        @NotBlank(message = "Notes are required") String resolutionNotes
) {
}
