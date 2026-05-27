package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemTypeResponseDTO;
import com.unimag.unimagleisureinventory.services.ItemTypeService;
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
@RequestMapping("/item-types")
@RequiredArgsConstructor
@Tag(name = "Item Types", description = "Endpoints for managing item type categories")
public class ItemTypeController {

    private final ItemTypeService itemTypeService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    @Operation(summary = "Get item type by ID", description = "Returns a single item type by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item type found"),
            @ApiResponse(responseCode = "404", description = "Item type not found")
    })
    public ResponseEntity<ItemTypeResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(itemTypeService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    @Operation(summary = "List all item types", description = "Returns all available item type categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item types retrieved successfully")
    })
    public ResponseEntity<List<ItemTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(itemTypeService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Create item type", description = "Creates a new item type category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item type created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<ItemTypeResponseDTO> create(
            @Valid @RequestBody CreateItemTypeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemTypeService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Update item type", description = "Updates an existing item type category by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item type updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Item type not found")
    })
    public ResponseEntity<ItemTypeResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateItemTypeRequestDTO request) {
        return ResponseEntity.ok(itemTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Delete item type", description = "Deletes an item type category by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item type deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item type not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        itemTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
