package com.unimag.unimagleisureinventory.repositories.logs;

import com.unimag.unimagleisureinventory.model.checkout.CheckOutStatusLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CheckOutStatusLogsRepository extends JpaRepository<CheckOutStatusLogs, UUID> {

    // Ver historial completo de cambios de un préstamo (RNF-10)
    List<CheckOutStatusLogs> findByCheckout_ChekOutId(UUID checkoutId);

    // Ver todas las acciones realizadas por un auxiliar
    List<CheckOutStatusLogs> findByTriggeredBy_Id(UUID clerkId);

    // Auditoría por rango de fechas (RF-30 reportes)
    List<CheckOutStatusLogs> findByRecordedAtBetween(LocalDateTime from, LocalDateTime to);
}
