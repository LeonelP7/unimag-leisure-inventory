package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemTypeResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ItemTypeService {
    List<ItemTypeResponseDTO> getAll();
    ItemTypeResponseDTO create(CreateItemTypeRequestDTO request);
    ItemTypeResponseDTO update(UUID id, CreateItemTypeRequestDTO request);
    void delete(UUID id);
}
