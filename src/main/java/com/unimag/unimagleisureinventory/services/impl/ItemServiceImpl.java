package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemResponseDTO;
import com.unimag.unimagleisureinventory.mappers.ItemMapper;
import com.unimag.unimagleisureinventory.model.item.Item;
import com.unimag.unimagleisureinventory.model.item.ItemType;
import com.unimag.unimagleisureinventory.repositories.ItemRepository;
import com.unimag.unimagleisureinventory.repositories.ItemTypeRepository;
import com.unimag.unimagleisureinventory.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemTypeRepository itemTypeRepository;
    private final ItemMapper itemMapper;

    // RF-05 — listar disponibles o todos con filtros opcionales (RF-06)
    public List<ItemResponseDTO> getItems(String name, UUID itemTypeId) {
        if (name != null && itemTypeId != null) {
            return itemRepository.findByNameContainingIgnoreCaseAndItemType_IdItemType(name, itemTypeId)
                    .stream().map(itemMapper::toResponseDTO).toList();
        }
        if (name != null) {
            return itemRepository.findByNameContainingIgnoreCase(name)
                    .stream().map(itemMapper::toResponseDTO).toList();
        }
        if (itemTypeId != null) {
            return itemRepository.findByItemType_IdItemType(itemTypeId)
                    .stream().map(itemMapper::toResponseDTO).toList();
        }
        return itemRepository.findAvailableItems()
                .stream().map(itemMapper::toResponseDTO).toList();
    }

    // RF-31 — agregar artículo
    public ItemResponseDTO createItem(CreateItemRequestDTO request) {
        ItemType itemType = itemTypeRepository.findById(request.itemTypeId())
                .orElseThrow(() -> new RuntimeException("ItemType not found"));

        Item item = itemMapper.toEntity(request);
        item.setItemType(itemType);
        item.setAvailableQuantity(request.totalQuantity()); // al crear, todos disponibles

        return itemMapper.toResponseDTO(itemRepository.save(item));
    }

    // RF-31 — editar artículo
    public ItemResponseDTO updateItem(UUID itemId, CreateItemRequestDTO request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        ItemType itemType = itemTypeRepository.findById(request.itemTypeId())
                .orElseThrow(() -> new RuntimeException("ItemType not found"));

        item.setName(request.name());
        item.setDescription(request.description());
        item.setTotalQuantity(request.totalQuantity());
        item.setItemType(itemType);
        item.setItemStatus(request.itemStatus());
        item.setItemCondition(request.itemCondition());

        return itemMapper.toResponseDTO(itemRepository.save(item));
    }

    // RF-31 — eliminar artículo
    public void deleteItem(UUID itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new RuntimeException("Item not found");
        }
        itemRepository.deleteById(itemId);
    }
}
