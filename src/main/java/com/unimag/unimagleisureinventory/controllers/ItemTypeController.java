package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemTypeResponseDTO;
import com.unimag.unimagleisureinventory.services.ItemTypeService;
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
public class ItemTypeController {

    private final ItemTypeService itemTypeService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    public ResponseEntity<ItemTypeResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(itemTypeService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK', 'STUDENT')")
    public ResponseEntity<List<ItemTypeResponseDTO>> getAll() {
        return ResponseEntity.ok(itemTypeService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<ItemTypeResponseDTO> create(
            @Valid @RequestBody CreateItemTypeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemTypeService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<ItemTypeResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateItemTypeRequestDTO request) {
        return ResponseEntity.ok(itemTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        itemTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
