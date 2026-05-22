package com.unimag.unimagleisureinventory.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePenaltyRequestDTO(
        @NotNull(message = "Student id is required") Long studentId,
        @NotNull(message = "Check out is required") UUID checkOutId,
        @NotNull(message = "Penalty type is required") UUID penaltyTypeId,
        @NotBlank(message = "Reason is required") String reason
) {
}
