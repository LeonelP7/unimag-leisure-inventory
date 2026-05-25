package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PenaltyRepository extends JpaRepository<Penalty, UUID> {

    // Historial de sanciones del estudiante (RF-29)
    @Query("SELECT p FROM Penalty p WHERE p.student.studentId = :studentId")
    List<Penalty> findByStudentId(Long studentId);

    // Verificar si el estudiante tiene una sanción activa (RF-24)
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Penalty p WHERE p.student.studentId = :studentId AND p.penaltyStatus = :status")
    boolean existsByStudentIdAndStatus(Long studentId, PenaltyStatus status);

    // Consulta del auxiliar antes de aprobar entrega (RF-25)
    @Query("SELECT p FROM Penalty p WHERE p.student.studentId = :studentId AND p.penaltyStatus = :status")
    List<Penalty> findByStudentIdAndStatus(Long studentId, PenaltyStatus status);

    // Sanciones por tipo, útil para reportes (RF-30)
    List<Penalty> findByPenaltyTypeId(UUID penaltyTypeId);

    List<Penalty> findByStartDateBetween(LocalDateTime from, LocalDateTime to);
}
