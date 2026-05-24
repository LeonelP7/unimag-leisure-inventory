package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.enums.ItemStatus;
import com.unimag.unimagleisureinventory.model.item.ItemCondigionLogs;
import com.unimag.unimagleisureinventory.model.item.ItemStatusLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ItemStatusLogsRepository extends JpaRepository<ItemStatusLogs, UUID> {

    // Historial de cambios de estado de un artículo (RNF-10)
    List<ItemStatusLogs> findByItem_ItemId(UUID itemId);

    // Acciones realizadas por un auxiliar
    List<ItemStatusLogs> findByTriggeredBy_Id(UUID clerkId);

    // Artículos que cambiaron a un estado específico en un rango de fechas (RF-30)
    List<ItemStatusLogs> findByNewStatusAndRecordedAtBetween(
            ItemStatus status,
            LocalDateTime from,
            LocalDateTime to
    );
}
