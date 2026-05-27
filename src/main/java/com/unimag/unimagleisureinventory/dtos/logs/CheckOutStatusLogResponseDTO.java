package com.unimag.unimagleisureinventory.dtos.logs;

import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;
import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CheckOutStatusLogResponseDTO(
        UUID id,
        CheckOutStatus previousStatus,
        CheckOutStatus newStatus,
        LocalDateTime recordedAt,
        PersonResponseDTO triggeredBy
) {
}
