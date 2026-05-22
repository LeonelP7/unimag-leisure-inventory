package com.unimag.unimagleisureinventory.dtos.reservation;

import com.unimag.unimagleisureinventory.dtos.item.ItemResponseDTO;
import com.unimag.unimagleisureinventory.dtos.student.StudentResponseDTO;
import com.unimag.unimagleisureinventory.model.enums.ReservationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponseDTO(
        UUID id,
        LocalDateTime startDate,
        LocalDateTime claimDeadline,
        StudentResponseDTO student,
        ItemResponseDTO item,
        ReservationStatus status
) {
}
