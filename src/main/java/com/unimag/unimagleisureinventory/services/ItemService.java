package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ItemService {
    List<ItemResponseDTO> getItems(String name, UUID itemTypeId);
    ItemResponseDTO createItem(CreateItemRequestDTO request);
    ItemResponseDTO updateItem(UUID itemId, CreateItemRequestDTO request);
    void deleteItem(UUID itemId);
}
