package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CheckOutRepository extends JpaRepository<CheckOut, UUID> {

    // Historial de préstamos del estudiante (RF-27)
    @Query("SELECT c FROM CheckOut c WHERE c.student.studentId = :studentId")
    List<CheckOut> findByStudentId(Long studentId);

    // Historial de préstamos por auxiliar (RF-28)
    @Query("SELECT c FROM CheckOut c WHERE c.student.studentId = :studentId AND c.status = :status")
    List<CheckOut> findByStudentIdAndStatus(Long studentId, CheckOutStatus status);

    // Préstamos vencidos sin devolución para generar alertas (RF-19)
    List<CheckOut> findByStatusAndDueDateBefore(CheckOutStatus status, LocalDateTime dateTime);

    // Verificar si un item está actualmente en préstamo
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CheckOut c WHERE c.reservation.item.itemId = :itemId AND c.status = :status")
    boolean existsByItemIdAndStatus(UUID itemId, CheckOutStatus status);

    List<CheckOut> findByCheckOutDateBetween(LocalDateTime from, LocalDateTime to);
}
