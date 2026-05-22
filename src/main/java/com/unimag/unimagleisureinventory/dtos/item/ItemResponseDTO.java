package com.unimag.unimagleisureinventory.dtos.item;

import com.unimag.unimagleisureinventory.model.enums.ItemCondition;
import com.unimag.unimagleisureinventory.model.enums.ItemStatus;

import java.util.UUID;

public record ItemResponseDTO(
        UUID itemId,
        String name,
        String description,
        int totalQuantity,
        int availableQuantity,
        ItemTypeResponseDTO itemType,
        ItemStatus itemStatus,
        ItemCondition itemCondition
) {
}
