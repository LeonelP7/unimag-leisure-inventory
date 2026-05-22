package com.unimag.unimagleisureinventory.dtos.reservation;

import jakarta.validation.constraints.NotNull;

public record ApproveReservationRequestDTO(
        @NotNull(message = "Must say if is approved or not") Boolean approved,
        String rejectionReason
) {
}
