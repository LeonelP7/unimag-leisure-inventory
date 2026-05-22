package com.unimag.unimagleisureinventory.mappers;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemResponseDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemTypeResponseDTO;
import com.unimag.unimagleisureinventory.model.item.Item;
import com.unimag.unimagleisureinventory.model.item.ItemType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(source = "itemType", target = "itemType")
    ItemResponseDTO toResponseDTO(Item item);

    ItemTypeResponseDTO toItemTypeResponseDTO(ItemType itemType);

    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "availableQuantity", ignore = true) // se asigna en el service
    @Mapping(target = "itemType", ignore = true) // se busca en el service
    Item toEntity(CreateItemRequestDTO request);
}
