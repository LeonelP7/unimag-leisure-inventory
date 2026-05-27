package com.unimag.unimagleisureinventory.dtos.item;

import java.util.UUID;

public record ItemTypeResponseDTO(
        UUID itemTypeId,
        String name
) {
}
