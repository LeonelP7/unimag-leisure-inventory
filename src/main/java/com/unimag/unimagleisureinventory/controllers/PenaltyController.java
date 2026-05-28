package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.dtos.penalty.CreatePenaltyRequestDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.PenaltyResponseDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.ResolvePenaltyRequestDTO;
import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.services.PenaltyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Penalties", description = "Endpoints for managing student sanctions")
public class PenaltyController {

    private final PenaltyService penaltyService;
    private final SecurityUtils securityUtils;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Get penalty by ID", description = "Returns a single penalty record by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalty found"),
            @ApiResponse(responseCode = "404", description = "Penalty not found")
    })
    public ResponseEntity<PenaltyResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(penaltyService.getById(id));
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Get all penalties", description = "Returns a list of all sanctions registered in the system (RF-29)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalties retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    public ResponseEntity<List<PenaltyResponseDTO>> getAll() {
        return ResponseEntity.ok(penaltyService.getAll());
    }

    // RF-21/RF-22 — activar sanción
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Create penalty", description = "Activates a new sanction for a student (RF-21/RF-22)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Penalty created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<PenaltyResponseDTO> create(
            @Valid @RequestBody CreatePenaltyRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(penaltyService.create(request));
    }

    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasRole('INVENTORY_CLERK')")
    @Operation(summary = "Resolve penalty", description = "Closes an active sanction (RF-24)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalty resolved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Penalty not found")
    })
    public ResponseEntity<PenaltyResponseDTO> resolve(
            @PathVariable UUID id,
            @Valid @RequestBody ResolvePenaltyRequestDTO request) {
        return ResponseEntity.ok(penaltyService.resolve(id, request));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Get penalties by student", description = "Returns the full sanction history for a given student (RF-29)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalties retrieved successfully")
    })
    public ResponseEntity<List<PenaltyResponseDTO>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(penaltyService.getByStudent(studentId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get my penalties", description = "Returns the sanction history for the authenticated student (RF-29)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalties retrieved successfully")
    })
    public ResponseEntity<List<PenaltyResponseDTO>> getMySanctions(
            HttpServletRequest request) {
        Long studentId = securityUtils.getCurrentStudentId(request);
        return ResponseEntity.ok(penaltyService.getByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Check active penalty", description = "Returns whether a student currently has an active sanction (RF-25)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active penalty status returned")
    })
    public ResponseEntity<Boolean> hasActivePenalty(@PathVariable Long studentId) {
        return ResponseEntity.ok(penaltyService.hasActivePenalty(studentId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Get all penalties", description = "Returns all sanctions with optional filter by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalties retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    public ResponseEntity<List<PenaltyResponseDTO>> getAll(
            @RequestParam(required = false) PenaltyStatus status) {
        return ResponseEntity.ok(penaltyService.getAll(status));
    }
}
