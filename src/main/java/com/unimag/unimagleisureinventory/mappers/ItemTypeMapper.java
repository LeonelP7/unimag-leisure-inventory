package com.unimag.unimagleisureinventory.mappers;

import com.unimag.unimagleisureinventory.dtos.item.CreateItemTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.item.ItemTypeResponseDTO;
import com.unimag.unimagleisureinventory.model.item.ItemType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemTypeMapper {

    ItemTypeResponseDTO toResponseDTO(ItemType itemType);

    @Mapping(target = "itemTypeId", ignore = true)
    ItemType toEntity(CreateItemTypeRequestDTO request);
}
