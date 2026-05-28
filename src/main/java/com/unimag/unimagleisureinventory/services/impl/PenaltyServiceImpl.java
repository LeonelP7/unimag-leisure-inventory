package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.dtos.penalty.CreatePenaltyRequestDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.PenaltyResponseDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.ResolvePenaltyRequestDTO;
import com.unimag.unimagleisureinventory.exceptions.BusinessException;
import com.unimag.unimagleisureinventory.exceptions.ResourceNotFoundException;
import com.unimag.unimagleisureinventory.mappers.PenaltyMapper;
import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyType;
import com.unimag.unimagleisureinventory.model.person.Student;
import com.unimag.unimagleisureinventory.repositories.CheckOutRepository;
import com.unimag.unimagleisureinventory.repositories.PenaltyRepository;
import com.unimag.unimagleisureinventory.repositories.PenaltyTypeRepository;
import com.unimag.unimagleisureinventory.repositories.StudentRepository;
import com.unimag.unimagleisureinventory.services.AuditLogService;
import com.unimag.unimagleisureinventory.services.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PenaltyServiceImpl implements PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final StudentRepository studentRepository;
    private final CheckOutRepository checkOutRepository;
    private final PenaltyTypeRepository penaltyTypeRepository;
    private final PenaltyMapper penaltyMapper;
    private final AuditLogService  auditLogService;

    public PenaltyResponseDTO getById(UUID id) {
        return penaltyMapper.toResponseDTO(
                penaltyRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Penalty not found"))
        );
    }

    @Override
    public List<PenaltyResponseDTO> getAll() {
        return penaltyRepository.findAll().stream().map(penaltyMapper::toResponseDTO).toList();
    }

    // RF-21/RF-22 — activar sanción manualmente
    @Transactional
    public PenaltyResponseDTO create(CreatePenaltyRequestDTO request) {

        // verificar que no tenga ya una sanción activa
        if (penaltyRepository.existsByStudentIdAndStatus(
                request.studentId(), PenaltyStatus.ACTIVE)) {
            throw new BusinessException("Student already has an active penalty");
        }

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        CheckOut checkOut = checkOutRepository.findById(request.checkOutId())
                .orElseThrow(() -> new ResourceNotFoundException("CheckOut not found"));

        PenaltyType penaltyType = penaltyTypeRepository.findById(request.penaltyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Penalty type not found"));

        Penalty penalty = new Penalty();
        penalty.setStudent(student);
        penalty.setCheckOut(checkOut);
        penalty.setPenaltyType(penaltyType);
        penalty.setReason(request.reason());
        penalty.setStartDate(LocalDateTime.now());
        penalty.setPenaltyStatus(PenaltyStatus.ACTIVE);

        Penalty saved =  penaltyRepository.save(penalty);
        auditLogService.logPenaltyStatus(saved, null, PenaltyStatus.ACTIVE);

        return penaltyMapper.toResponseDTO(saved);
    }

    // RF-24 — cerrar sanción
    @Transactional
    public PenaltyResponseDTO resolve(UUID penaltyId, ResolvePenaltyRequestDTO request) {
        Penalty penalty = penaltyRepository.findById(penaltyId)
                .orElseThrow(() -> new ResourceNotFoundException("Penalty not found"));

        if (penalty.getPenaltyStatus() != PenaltyStatus.ACTIVE) {
            throw new BusinessException("Penalty is not active");
        }

        PenaltyStatus previous = penalty.getPenaltyStatus();
        penalty.setPenaltyStatus(PenaltyStatus.RESOLVED);
        penalty.setEndDate(LocalDateTime.now());
        Penalty saved = penaltyRepository.save(penalty);

        auditLogService.logPenaltyStatus(saved, previous, PenaltyStatus.RESOLVED);
        return penaltyMapper.toResponseDTO(saved);
    }

    // RF-29 — historial de sanciones del estudiante
    public List<PenaltyResponseDTO> getByStudent(Long studentId) {
        return penaltyRepository.findByStudentId(studentId)
                .stream()
                .map(penaltyMapper::toResponseDTO)
                .toList();
    }

    // RF-25 — verificar sanción activa
    public boolean hasActivePenalty(Long studentId) {
        return penaltyRepository.existsByStudentIdAndStatus(
                studentId, PenaltyStatus.ACTIVE);
    }

    public List<PenaltyResponseDTO> getAll(PenaltyStatus status) {
        List<Penalty> results = status != null
                ? penaltyRepository.findByPenaltyStatus(status)
                : penaltyRepository.findAll();

        return results.stream().map(penaltyMapper::toResponseDTO).toList();
    }
}
