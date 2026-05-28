package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.dtos.penalty.CreatePenaltyTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.PenaltyTypeResponseDTO;
import com.unimag.unimagleisureinventory.services.PenaltyTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Penalty Types", description = "Endpoints for managing penalty type categories")
public class PenaltyTypeController {

    private final PenaltyTypeService penaltyTypeService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Get penalty type by ID", description = "Returns a single penalty type by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalty type found"),
            @ApiResponse(responseCode = "404", description = "Penalty type not found")
    })
    public ResponseEntity<PenaltyTypeResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(penaltyTypeService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "List all penalty types", description = "Returns all available penalty type categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalty types retrieved successfully")
    })
    public ResponseEntity<List<PenaltyTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(penaltyTypeService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create penalty type", description = "Creates a new penalty type category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Penalty type created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<PenaltyTypeResponseDTO> create(
            @Valid @RequestBody CreatePenaltyTypeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(penaltyTypeService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update penalty type", description = "Updates an existing penalty type category by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Penalty type updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Penalty type not found")
    })
    public ResponseEntity<PenaltyTypeResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreatePenaltyTypeRequestDTO request) {
        return ResponseEntity.ok(penaltyTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete penalty type", description = "Deletes a penalty type category by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Penalty type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Penalty type not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        penaltyTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
