package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.enums.ItemCondition;
import com.unimag.unimagleisureinventory.model.item.ItemCondigionLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ItemConditionLogsRepository extends JpaRepository<ItemCondigionLogs, UUID> {

    // Historial de condición de un artículo específico (RNF-10)
    List<ItemCondigionLogs> findByItem_ItemId(UUID itemId);

    // Acciones realizadas por un auxiliar
    List<ItemCondigionLogs> findByRegisteredBy_Id(UUID clerkId);

    // Artículos devueltos en mal estado en un rango de fechas (RF-30)
    List<ItemCondigionLogs> findByNewConditionAndRecordedAtBetween(
            ItemCondition condition,
            LocalDateTime from,
            LocalDateTime to
    );
}
