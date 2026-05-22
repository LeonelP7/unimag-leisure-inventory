package com.unimag.unimagleisureinventory.dtos.item;

import java.util.UUID;

public record ItemTypeResponseDTO(
        UUID idItemType,
        String name
) {
}
