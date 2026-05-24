package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemResponseDTO;
import com.unimag.unimagleisureinventory.services.ItemService;
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
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    public ResponseEntity<ItemResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(itemService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    public ResponseEntity<List<ItemResponseDTO>> getItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID itemTypeId) {
        return ResponseEntity.ok(itemService.getItems(name, itemTypeId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<ItemResponseDTO> createItem(
            @Valid @RequestBody CreateItemRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.createItem(request));
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<ItemResponseDTO> updateItem(
            @PathVariable UUID itemId,
            @Valid @RequestBody CreateItemRequestDTO request) {
        return ResponseEntity.ok(itemService.updateItem(itemId, request));
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
