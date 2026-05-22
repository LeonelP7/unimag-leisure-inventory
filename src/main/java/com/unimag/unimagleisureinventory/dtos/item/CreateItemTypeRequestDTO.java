package com.unimag.unimagleisureinventory.dtos.item;

import jakarta.validation.constraints.NotBlank;

public record CreateItemTypeRequestDTO(
        @NotBlank(message = "Name is required") String name
) {
}
