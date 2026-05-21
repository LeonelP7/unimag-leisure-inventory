package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyStatusLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PenaltyStatusLogsRepository extends JpaRepository<PenaltyStatusLogs, UUID> {

    // Historial de cambios de una sanción específica (RNF-10)
    List<PenaltyStatusLogs> findByPenalty_Id(UUID penaltyId);

    // Acciones realizadas por un auxiliar
    List<PenaltyStatusLogs> findByTriggeredBy_Id(UUID clerkId);

    // Sanciones resueltas en un rango de fechas (RF-30)
    List<PenaltyStatusLogs> findByNewStatusAndRecordedAtBetween(
            PenaltyStatus status,
            LocalDateTime from,
            LocalDateTime to
    );
}
