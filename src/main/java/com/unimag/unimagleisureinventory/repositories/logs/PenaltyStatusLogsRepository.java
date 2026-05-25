package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyStatusLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PenaltyStatusLogsRepository extends JpaRepository<PenaltyStatusLogs, UUID> {

    @Query("SELECT l FROM PenaltyStatusLogs l WHERE l.penalty.penaltyId = :penaltyId")
    List<PenaltyStatusLogs> findByPenaltyId(UUID penaltyId);

    @Query("SELECT l FROM PenaltyStatusLogs l WHERE l.triggeredBy.personId = :clerkId")
    List<PenaltyStatusLogs> findByTriggeredById(UUID clerkId);

    List<PenaltyStatusLogs> findByNewStatusAndRecordedAtBetween(
            PenaltyStatus status, LocalDateTime from, LocalDateTime to);
}
