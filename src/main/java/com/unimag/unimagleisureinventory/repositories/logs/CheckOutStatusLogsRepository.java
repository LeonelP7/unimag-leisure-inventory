package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.checkout.CheckOutStatusLogs;
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
}
