package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.enums.ItemStatus;
import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.model.item.ItemCondigionLogs;
import com.unimag.unimagleisureinventory.model.item.ItemStatusLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ItemStatusLogsRepository extends JpaRepository<ItemStatusLogs, UUID> {

    @Query("SELECT l FROM ItemStatusLogs l WHERE l.item.itemId = :itemId")
    List<ItemStatusLogs> findByItemId(UUID itemId);

    @Query("SELECT l FROM ItemStatusLogs l WHERE l.triggeredBy.personId = :clerkId")
    List<ItemStatusLogs> findByTriggeredById(UUID clerkId);

    List<ItemStatusLogs> findByNewStatusAndRecordedAtBetween(
            ItemStatus status, LocalDateTime from, LocalDateTime to);

    @Query("SELECT l FROM ItemStatusLogs l WHERE " +
            "(:from IS NULL OR l.recordedAt >= :from) AND " +
            "(:to IS NULL OR l.recordedAt <= :to) AND " +
            "(:role IS NULL OR l.triggeredBy.role = :role) AND " +
            "(:newStatus IS NULL OR l.newStatus = :newStatus)")
    List<ItemStatusLogs> findWithFilters(
            LocalDateTime from, LocalDateTime to,
            Role role, ItemStatus newStatus);
}
