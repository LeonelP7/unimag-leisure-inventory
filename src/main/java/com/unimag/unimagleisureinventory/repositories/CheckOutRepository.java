package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CheckOutRepository extends CrudRepository<CheckOut, UUID> {

    // Historial de préstamos del estudiante (RF-27)
    List<CheckOut> findByReservation_Student_Id(Long studentId);

    // Historial de préstamos por auxiliar (RF-28)
    List<CheckOut> findByReservation_Student_IdAndStatus(Long studentId, CheckOutStatus status);

    // Préstamos vencidos sin devolución para generar alertas (RF-19)
    List<CheckOut> findByStatusAndDueDateBefore(CheckOutStatus status, LocalDateTime dateTime);

    // Verificar si un item está actualmente en préstamo
    boolean existsByReservation_Item_ItemIdAndStatus(UUID itemId, CheckOutStatus status);

    List<CheckOut> findByCheckOutDateBetween(LocalDateTime from, LocalDateTime to);
}
