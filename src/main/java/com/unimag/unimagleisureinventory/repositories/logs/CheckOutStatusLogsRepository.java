package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.checkout.CheckOutStatusLogs;
import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;
import com.unimag.unimagleisureinventory.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CheckOutStatusLogsRepository extends JpaRepository<CheckOutStatusLogs, UUID> {

    @Query("SELECT l FROM CheckOutStatusLogs l WHERE l.checkout.checkOutId = :checkoutId")
    List<CheckOutStatusLogs> findByCheckoutId(UUID checkoutId);

    @Query("SELECT l FROM CheckOutStatusLogs l WHERE l.triggeredBy.personId = :clerkId")
    List<CheckOutStatusLogs> findByTriggeredById(UUID clerkId);

    List<CheckOutStatusLogs> findByRecordedAtBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT l FROM CheckOutStatusLogs l WHERE " +
            "(cast(:from as java.time.LocalDateTime) IS NULL OR l.recordedAt >= :from) AND " +
            "(cast(:to as java.time.LocalDateTime) IS NULL OR l.recordedAt <= :to) AND " +
            "(cast(:role as string) IS NULL OR l.triggeredBy.role = :role) AND " +
            "(cast(:newStatus as string) IS NULL OR l.newStatus = :newStatus)")
    List<CheckOutStatusLogs> findWithFilters(
            LocalDateTime from, LocalDateTime to,
            Role role, CheckOutStatus newStatus);
}
