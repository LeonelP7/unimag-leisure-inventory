package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.reservation.ApproveReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.CreateReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.ReservationResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    ReservationResponseDTO create(CreateReservationRequestDTO request, Long studentId);
    List<ReservationResponseDTO> getMyReservations(Long studentId);
    void cancel(UUID reservationId, Long studentId);
    ReservationResponseDTO verify(UUID reservationId);
    ReservationResponseDTO approve(UUID reservationId, ApproveReservationRequestDTO request);
    ReservationResponseDTO getById(UUID id);
    ReservationResponseDTO getPendingByStudent(Long studentId);
}
