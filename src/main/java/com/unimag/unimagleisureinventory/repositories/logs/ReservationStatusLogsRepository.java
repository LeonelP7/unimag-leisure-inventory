package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.enums.ReservationStatus;
import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.model.reservation.ReservationStatusLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationStatusLogsRepository extends JpaRepository<ReservationStatusLogs, UUID> {

    @Query("SELECT l FROM ReservationStatusLogs l WHERE l.reservation.id = :reservationId")
    List<ReservationStatusLogs> findByReservationId(UUID reservationId);

    @Query("SELECT l FROM ReservationStatusLogs l WHERE l.triggeredBy.personId = :clerkId")
    List<ReservationStatusLogs> findByTriggeredById(UUID clerkId);

    List<ReservationStatusLogs> findByNewStatusAndRecordedAtBetween(
            ReservationStatus status, LocalDateTime from, LocalDateTime to);

    @Query("SELECT l FROM ReservationStatusLogs l WHERE " +
            "(cast(:from as java.time.LocalDateTime) IS NULL OR l.recordedAt >= :from) AND " +
            "(cast(:to as java.time.LocalDateTime)IS NULL OR l.recordedAt <= :to) AND " +
            "(cast(:role as string) IS NULL OR l.triggeredBy.role = :role) AND " +
            "(cast(:newStatus as string) IS NULL OR l.newStatus = :newStatus)")
    List<ReservationStatusLogs> findWithFilters(
            LocalDateTime from, LocalDateTime to,
            Role role, ReservationStatus newStatus);
}
