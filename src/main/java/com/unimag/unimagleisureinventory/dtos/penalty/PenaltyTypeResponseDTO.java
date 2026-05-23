package com.unimag.unimagleisureinventory.dtos.penalty;

import java.util.UUID;

public record PenaltyTypeResponseDTO(
        UUID idPenaltyType,
        String name
) {
}
