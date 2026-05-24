package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.config.security.JwtUtil;
import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.dtos.reservation.ApproveReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.CreateReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.ReservationResponseDTO;
import com.unimag.unimagleisureinventory.services.ReservationService;
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
public class ReservationController {

    private final ReservationService reservationService;
    private final SecurityUtils securityUtils;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    public ResponseEntity<ReservationResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ReservationResponseDTO> create(
            @Valid @RequestBody CreateReservationRequestDTO request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.create(request, securityUtils.getCurrentStudentId(httpRequest)));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ReservationResponseDTO>> getMyReservations(
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(
                reservationService.getMyReservations(securityUtils.getCurrentStudentId(httpRequest)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> cancel(
            @PathVariable UUID id,
            HttpServletRequest httpRequest) {
        reservationService.cancel(id, securityUtils.getCurrentStudentId(httpRequest));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<ReservationResponseDTO> verify(@PathVariable UUID id) {
        return ResponseEntity.ok(reservationService.verify(id));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<ReservationResponseDTO> approve(
            @PathVariable UUID id,
            @Valid @RequestBody ApproveReservationRequestDTO request) {
        return ResponseEntity.ok(reservationService.approve(id, request));
    }
}
