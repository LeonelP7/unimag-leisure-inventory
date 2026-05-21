package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.enums.ReservationStatus;
import com.unimag.unimagleisureinventory.model.reservation.ReservationStatusLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationStatusLogsRepository extends JpaRepository<ReservationStatusLogs, UUID> {

    // Historial de cambios de una reserva específica (RNF-10)
    List<ReservationStatusLogs> findByReservation_Id(UUID reservationId);

    // Acciones realizadas por un auxiliar
    List<ReservationStatusLogs> findByTriggeredBy_Id(UUID clerkId);

    // Reservas que cambiaron a un estado en un rango de fechas (RF-30)
    List<ReservationStatusLogs> findByNewStatusAndRecordedAtBetween(
            ReservationStatus status,
            LocalDateTime from,
            LocalDateTime to
    );
}
