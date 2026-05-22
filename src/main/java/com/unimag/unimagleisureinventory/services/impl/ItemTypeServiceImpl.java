package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemTypeResponseDTO;
import com.unimag.unimagleisureinventory.mappers.ItemTypeMapper;
import com.unimag.unimagleisureinventory.model.item.ItemType;
import com.unimag.unimagleisureinventory.repositories.ItemTypeRepository;
import com.unimag.unimagleisureinventory.services.ItemTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemTypeServiceImpl implements ItemTypeService {

    private final ItemTypeRepository itemTypeRepository;
    private final ItemTypeMapper itemTypeMapper;

    public List<ItemTypeResponseDTO> getAll() {
        return itemTypeRepository.findAll()
                .stream()
                .map(itemTypeMapper::toResponseDTO)
                .toList();
    }

    public ItemTypeResponseDTO create(CreateItemTypeRequestDTO request) {
        if (itemTypeRepository.existsByName(request.name())) {
            throw new RuntimeException("Item type already exists");
        }
        return itemTypeMapper.toResponseDTO(
                itemTypeRepository.save(itemTypeMapper.toEntity(request))
        );
    }

    public ItemTypeResponseDTO update(UUID id, CreateItemTypeRequestDTO request) {
        ItemType itemType = itemTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item type not found"));

        if (itemTypeRepository.existsByName(request.name())) {
            throw new RuntimeException("Item type name already in use");
        }

        itemType.setName(request.name());
        return itemTypeMapper.toResponseDTO(itemTypeRepository.save(itemType));
    }

    public void delete(UUID id) {
        if (!itemTypeRepository.existsById(id)) {
            throw new RuntimeException("Item type not found");
        }
        itemTypeRepository.deleteById(id);
    }
}
