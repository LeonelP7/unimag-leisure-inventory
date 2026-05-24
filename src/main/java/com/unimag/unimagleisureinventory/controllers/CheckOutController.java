package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.dtos.checkout.CheckInRequestDTO;
import com.unimag.unimagleisureinventory.dtos.checkout.CheckOutResponseDTO;
import com.unimag.unimagleisureinventory.dtos.checkout.CreateCheckOutRequestDTO;
import com.unimag.unimagleisureinventory.services.CheckOutService;
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
@RequestMapping("/checkouts")
@RequiredArgsConstructor
public class CheckOutController {

    private final CheckOutService checkOutService;
    private final SecurityUtils securityUtils;

    // RF-13/RF-14 — registrar préstamo (auxiliar)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<CheckOutResponseDTO> create(
            @Valid @RequestBody CreateCheckOutRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(checkOutService.create(request));
    }

    // RF-16 — registrar devolución (auxiliar) y RF-17 — evaluar estado del artículo (auxiliar)
    @PutMapping("/{id}/checkin")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<CheckOutResponseDTO> checkIn(
            @PathVariable UUID id,
            @Valid @RequestBody CheckInRequestDTO request) {
        return ResponseEntity.ok(checkOutService.checkIn(id, request));
    }

    // RF-27 — historial del propio estudiante
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CheckOutResponseDTO>> getMyCheckOuts(
            HttpServletRequest request) {
        Long studentId = securityUtils.getCurrentStudentId(request);
        return ResponseEntity.ok(checkOutService.getByStudent(studentId));
    }

    // RF-28 — historial de cualquier estudiante (auxiliar)
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<CheckOutResponseDTO>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(checkOutService.getByStudent(studentId));
    }
}
