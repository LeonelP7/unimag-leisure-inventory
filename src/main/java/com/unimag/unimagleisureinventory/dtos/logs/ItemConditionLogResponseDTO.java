package com.unimag.unimagleisureinventory.dtos.logs;

import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;
import com.unimag.unimagleisureinventory.model.enums.ItemCondition;

import java.time.LocalDateTime;
import java.util.UUID;

public record ItemConditionLogResponseDTO(
        UUID id,
        ItemCondition previousCondition,
        ItemCondition newCondition,
        String notes,
        LocalDateTime recordedAt,
        PersonResponseDTO registeredBy
) {
}
