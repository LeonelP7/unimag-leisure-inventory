package com.unimag.unimagleisureinventory.dtos.checkout;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCheckOutRequestDTO(
        UUID reservationId,        // opcional — si viene de una reserva
        UUID itemId,               // opcional — si es préstamo directo sin reserva
        @NotNull(message = "Student id is required") Long studentId    // requerido siempre
) {
}
