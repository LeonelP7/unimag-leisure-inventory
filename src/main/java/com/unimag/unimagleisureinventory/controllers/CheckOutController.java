package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.dtos.checkout.CheckInRequestDTO;
import com.unimag.unimagleisureinventory.dtos.checkout.CheckOutResponseDTO;
import com.unimag.unimagleisureinventory.dtos.checkout.CreateCheckOutRequestDTO;
import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;
import com.unimag.unimagleisureinventory.services.CheckOutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/checkouts")
@RequiredArgsConstructor
@Tag(name = "Checkouts", description = "Endpoints for managing item loans and returns")
public class CheckOutController {

    private final CheckOutService checkOutService;
    private final SecurityUtils securityUtils;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Get checkout by ID", description = "Returns a single checkout record by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkout found"),
            @ApiResponse(responseCode = "404", description = "Checkout not found")
    })
    public ResponseEntity<CheckOutResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(checkOutService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Register checkout", description = "Registers a new item loan (RF-13/RF-14)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Checkout registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<CheckOutResponseDTO> create(
            @Valid @RequestBody CreateCheckOutRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(checkOutService.create(request));
    }

    @PutMapping("/{id}/checkin")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Register check-in", description = "Registers an item return and evaluates its condition (RF-16/RF-17)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-in registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Checkout not found")
    })
    public ResponseEntity<CheckOutResponseDTO> checkIn(
            @PathVariable UUID id,
            @Valid @RequestBody CheckInRequestDTO request) {
        return ResponseEntity.ok(checkOutService.checkIn(id, request));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get my checkouts", description = "Returns the checkout history for the authenticated student (RF-27)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkout history retrieved successfully")
    })
    public ResponseEntity<List<CheckOutResponseDTO>> getMyCheckOuts(
            HttpServletRequest request) {
        Long studentId = securityUtils.getCurrentStudentId(request);
        return ResponseEntity.ok(checkOutService.getByStudent(studentId));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Get checkouts by student", description = "Returns the checkout history for a given student (RF-28)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkout history retrieved successfully")
    })
    public ResponseEntity<List<CheckOutResponseDTO>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(checkOutService.getByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Get active checkout by student", description = "Returns the current active loan for a specific student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active checkout found"),
            @ApiResponse(responseCode = "404", description = "No active checkout found for this student"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    public ResponseEntity<CheckOutResponseDTO> getActiveByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(checkOutService.getActiveByStudent(studentId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Get all checkouts", description = "Returns all loans with optional filters by status and start date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkouts retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    public ResponseEntity<List<CheckOutResponseDTO>> getAll(
            @RequestParam(required = false) CheckOutStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from) {
        return ResponseEntity.ok(checkOutService.getAll(status, from));
    }
}
