package com.unimag.unimagleisureinventory.dtos.penalty;

import jakarta.validation.constraints.NotBlank;

public record ResolvePenaltyRequestDTO(
        @NotBlank(message = "Notes are required") String resolutionNotes
) {
}
