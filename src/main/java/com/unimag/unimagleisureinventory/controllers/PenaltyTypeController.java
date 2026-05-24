package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.dtos.penalty.CreatePenaltyTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.PenaltyTypeResponseDTO;
import com.unimag.unimagleisureinventory.services.PenaltyTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/penalty-types")
@RequiredArgsConstructor
public class PenaltyTypeController {

    private final PenaltyTypeService penaltyTypeService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<PenaltyTypeResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(penaltyTypeService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<PenaltyTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(penaltyTypeService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PenaltyTypeResponseDTO> create(
            @Valid @RequestBody CreatePenaltyTypeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(penaltyTypeService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PenaltyTypeResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreatePenaltyTypeRequestDTO request) {
        return ResponseEntity.ok(penaltyTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        penaltyTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
