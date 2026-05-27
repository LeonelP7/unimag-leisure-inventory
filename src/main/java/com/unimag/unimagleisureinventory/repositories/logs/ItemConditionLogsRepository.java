package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.enums.ItemCondition;
import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.model.item.ItemCondigionLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ItemConditionLogsRepository extends JpaRepository<ItemCondigionLogs, UUID> {

    @Query("SELECT l FROM ItemCondigionLogs l WHERE l.item.itemId = :itemId")
    List<ItemCondigionLogs> findByItemId(UUID itemId);

    @Query("SELECT l FROM ItemCondigionLogs l WHERE l.registeredBy.personId = :clerkId")
    List<ItemCondigionLogs> findByRegisteredById(UUID clerkId);

    List<ItemCondigionLogs> findByNewConditionAndRecordedAtBetween(
            ItemCondition condition, LocalDateTime from, LocalDateTime to);

    @Query("SELECT l FROM ItemCondigionLogs l WHERE " +
            "(:from IS NULL OR l.recordedAt >= :from) AND " +
            "(:to IS NULL OR l.recordedAt <= :to) AND " +
            "(:role IS NULL OR l.registeredBy.role = :role) AND " +
            "(:newCondition IS NULL OR l.newCondition = :newCondition)")
    List<ItemCondigionLogs> findWithFilters(
            LocalDateTime from, LocalDateTime to,
            Role role, ItemCondition newCondition);
}
