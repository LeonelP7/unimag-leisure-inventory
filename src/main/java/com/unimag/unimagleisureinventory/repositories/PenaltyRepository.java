package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyType;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PenaltyRepository extends CrudRepository<Penalty, UUID> {

    // Historial de sanciones del estudiante (RF-29)
    List<Penalty> findByStudent_Id(Long studentId);

    // Verificar si el estudiante tiene una sanción activa (RF-24)
    boolean existsByStudent_IdAndStatus(Long studentId, PenaltyStatus status);

    // Consulta del auxiliar antes de aprobar entrega (RF-25)
    List<Penalty> findByStudent_IdAndStatus(Long studentId, PenaltyStatus status);

    // Sanciones por tipo, útil para reportes (RF-30)
    List<Penalty> findByPenaltyType_Id(UUID penaltyTypeId);

    List<Penalty> findByStartDateBetween(LocalDateTime from, LocalDateTime to);
}
