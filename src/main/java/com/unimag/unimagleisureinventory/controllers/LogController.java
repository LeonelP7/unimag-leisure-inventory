package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.dtos.logs.*;
import com.unimag.unimagleisureinventory.model.enums.*;
import com.unimag.unimagleisureinventory.services.LogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@Tag(name = "Logs", description = "Consulta de logs de auditoría")
public class LogController {

    private final LogService logService;

    @GetMapping("/reservations")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<ReservationStatusLogResponseDTO>> getReservationLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) ReservationStatus newStatus) {
        return ResponseEntity.ok(logService.getReservationLogs(from, to, role, newStatus));
    }

    @GetMapping("/checkouts")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<CheckOutStatusLogResponseDTO>> getCheckOutLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) CheckOutStatus newStatus) {
        return ResponseEntity.ok(logService.getCheckOutLogs(from, to, role, newStatus));
    }

    @GetMapping("/penalties")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<PenaltyStatusLogResponseDTO>> getPenaltyLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) PenaltyStatus newStatus) {
        return ResponseEntity.ok(logService.getPenaltyLogs(from, to, role, newStatus));
    }

    @GetMapping("/items/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<ItemStatusLogResponseDTO>> getItemStatusLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) ItemStatus newStatus) {
        return ResponseEntity.ok(logService.getItemStatusLogs(from, to, role, newStatus));
    }

    @GetMapping("/items/condition")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<ItemConditionLogResponseDTO>> getItemConditionLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) ItemCondition newCondition) {
        return ResponseEntity.ok(logService.getItemConditionLogs(from, to, role, newCondition));
    }
}
