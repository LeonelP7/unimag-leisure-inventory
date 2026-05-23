package com.unimag.unimagleisureinventory.dtos.penalty;

import com.unimag.unimagleisureinventory.dtos.student.StudentResponseDTO;
import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PenaltyResponseDTO(
        UUID penaltyId,
        String reason,
        LocalDateTime startDate,
        LocalDateTime endDate,
        PenaltyStatus penaltyStatus,
        PenaltyTypeResponseDTO penaltyType,
        StudentResponseDTO student
) {
}
