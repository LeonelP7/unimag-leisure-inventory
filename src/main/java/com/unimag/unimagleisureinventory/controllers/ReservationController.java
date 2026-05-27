package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.config.security.JwtUtil;
import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.dtos.reservation.ApproveReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.CreateReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.ReservationResponseDTO;
import com.unimag.unimagleisureinventory.services.ReservationService;
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
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Endpoints for managing item reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final SecurityUtils securityUtils;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    @Operation(summary = "Get reservation by ID", description = "Returns a single reservation by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Create reservation", description = "Creates a new reservation for the authenticated student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reservation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<ReservationResponseDTO> create(
            @Valid @RequestBody CreateReservationRequestDTO request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.create(request, securityUtils.getCurrentStudentId(httpRequest)));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Get my reservations", description = "Returns all reservations for the authenticated student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully")
    })
    public ResponseEntity<List<ReservationResponseDTO>> getMyReservations(
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(
                reservationService.getMyReservations(securityUtils.getCurrentStudentId(httpRequest)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Cancel reservation", description = "Cancels a reservation belonging to the authenticated student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<Void> cancel(
            @PathVariable UUID id,
            HttpServletRequest httpRequest) {
        reservationService.cancel(id, securityUtils.getCurrentStudentId(httpRequest));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Verify reservation", description = "Verifies the validity of a reservation before checkout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation verified successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationResponseDTO> verify(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.verify(id));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Approve reservation", description = "Approves a pending reservation and assigns items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation approved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationResponseDTO> approve(
            @PathVariable UUID id,
            @Valid @RequestBody ApproveReservationRequestDTO request) {
        return ResponseEntity.ok(reservationService.approve(id, request));
    }
}
