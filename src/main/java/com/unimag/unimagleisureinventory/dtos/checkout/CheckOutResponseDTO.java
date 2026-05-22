package com.unimag.unimagleisureinventory.dtos.checkout;

import com.unimag.unimagleisureinventory.dtos.reservation.ReservationResponseDTO;
import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;
import com.unimag.unimagleisureinventory.model.enums.ItemCondition;

import java.time.LocalDateTime;
import java.util.UUID;

public record CheckOutResponseDTO(
        UUID checkOutId,
        LocalDateTime checkOutDate,
        LocalDateTime dueDate,
        LocalDateTime checkInDate,
        CheckOutStatus status,
        ItemCondition returnedItemCondition,
        ReservationResponseDTO reservation
) {
}
