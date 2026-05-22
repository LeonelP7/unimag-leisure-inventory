package com.unimag.unimagleisureinventory.dtos.reservation;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateReservationRequestDTO(
        @NotNull(message = "Item is required") UUID itemId,
        @NotNull(message = "Reservation time is required") LocalDateTime reservationTime
) {
}
