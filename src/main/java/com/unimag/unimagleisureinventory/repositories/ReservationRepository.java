package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.enums.ReservationStatus;
import com.unimag.unimagleisureinventory.model.item.ItemType;
import com.unimag.unimagleisureinventory.model.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByStudent_IdAndStatus(Long studentId,  ReservationStatus status);
    List<Reservation> findByStudent_Id(Long studentId);

    // RF-11 — cancelar reserva activa, buscar por item y status
    List<Reservation> findByItem_ItemIdAndStatus(UUID itemId, ReservationStatus status);

    // RF-19 — alertas por préstamos vencidos, buscar reservas cuyo claimDeadline ya pasó
    List<Reservation> findByStatusAndClaimDeadlineBefore(ReservationStatus status, LocalDateTime dateTime);

    // RF-07 — verificar si el estudiante ya tiene un préstamo activo antes de nueva reserva
    boolean existsByStudent_IdAndStatus(Long studentId, ReservationStatus status);
}
