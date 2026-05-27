package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.enums.ReservationStatus;
import com.unimag.unimagleisureinventory.model.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query("SELECT r FROM Reservation r WHERE r.student.studentId = :studentId AND r.status = :status")
    List<Reservation> findByStudentIdAndStatus(Long studentId, ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.student.studentId = :studentId")
    List<Reservation> findByStudentId(Long studentId);

    // RF-11 — cancelar reserva activa, buscar por item y status
    List<Reservation> findByItem_ItemIdAndStatus(UUID itemId, ReservationStatus status);

    // RF-19 — alertas por préstamos vencidos, buscar reservas cuyo claimDeadline ya pasó
    List<Reservation> findByStatusAndClaimDeadlineBefore(ReservationStatus status, LocalDateTime dateTime);

    // RF-07 — verificar si el estudiante ya tiene un préstamo activo antes de nueva reserva
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r WHERE r.student.studentId = :studentId AND r.status = :status")
    boolean existsByStudentIdAndStatus(Long studentId, ReservationStatus status);
}
