package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.dtos.penalty.CreatePenaltyRequestDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.PenaltyResponseDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.ResolvePenaltyRequestDTO;
import com.unimag.unimagleisureinventory.mappers.PenaltyMapper;
import com.unimag.unimagleisureinventory.services.PenaltyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/penalties")
@RequiredArgsConstructor
public class PenaltyController {

    private final PenaltyService penaltyService;
    private final SecurityUtils securityUtils;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<PenaltyResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(penaltyService.getById(id));
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<PenaltyResponseDTO>> getAll() {
        return ResponseEntity.ok(penaltyService.getAll());
    }

    // RF-21/RF-22 — activar sanción
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<PenaltyResponseDTO> create(
            @Valid @RequestBody CreatePenaltyRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(penaltyService.create(request));
    }

    // RF-24 — cerrar sanción
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasRole('INVENTORY_CLERK')")
    public ResponseEntity<PenaltyResponseDTO> resolve(
            @PathVariable UUID id,
            @Valid @RequestBody ResolvePenaltyRequestDTO request) {
        return ResponseEntity.ok(penaltyService.resolve(id, request));
    }

    // RF-29 — historial de sanciones
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<PenaltyResponseDTO>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(penaltyService.getByStudent(studentId));
    }

    // RF-29 — estudiante consulta sus propias sanciones
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<PenaltyResponseDTO>> getMySanctions(
            HttpServletRequest request) {
        Long studentId = securityUtils.getCurrentStudentId(request);
        return ResponseEntity.ok(penaltyService.getByStudent(studentId));
    }

    // RF-25 — verificar sanción activa
    @GetMapping("/student/{studentId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<Boolean> hasActivePenalty(@PathVariable Long studentId) {
        return ResponseEntity.ok(penaltyService.hasActivePenalty(studentId));
    }
}
