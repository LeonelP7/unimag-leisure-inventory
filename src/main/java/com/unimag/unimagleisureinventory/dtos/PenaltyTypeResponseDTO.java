package com.unimag.unimagleisureinventory.dtos;

import java.util.UUID;

public record PenaltyTypeResponseDTO(
        UUID idPenaltyType,
        String name
) {
}
