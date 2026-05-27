package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemResponseDTO;
import com.unimag.unimagleisureinventory.services.ItemService;
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
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "Endpoints for managing inventory items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    @Operation(summary = "Get item by ID", description = "Returns a single inventory item by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<ItemResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    @Operation(summary = "List items", description = "Returns items filtered optionally by name and/or item type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully")
    })
    public ResponseEntity<List<ItemResponseDTO>> getItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID itemTypeId) {
        return ResponseEntity.ok(itemService.getItems(name, itemTypeId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Create item", description = "Creates a new inventory item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<ItemResponseDTO> createItem(
            @Valid @RequestBody CreateItemRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.createItem(request));
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Update item", description = "Updates an existing inventory item by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<ItemResponseDTO> updateItem(
            @PathVariable UUID itemId,
            @Valid @RequestBody CreateItemRequestDTO request) {
        return ResponseEntity.ok(itemService.updateItem(itemId, request));
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    @Operation(summary = "Delete item", description = "Deletes an inventory item by its UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<Void> deleteItem(@PathVariable UUID itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
