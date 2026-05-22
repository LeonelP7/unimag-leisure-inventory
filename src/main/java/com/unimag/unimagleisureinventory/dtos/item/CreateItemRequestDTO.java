package com.unimag.unimagleisureinventory.dtos.item;

import com.unimag.unimagleisureinventory.model.enums.ItemCondition;
import com.unimag.unimagleisureinventory.model.enums.ItemStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateItemRequestDTO(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Description is required") String description,
        @NotNull(message = "Quantity cant be null") @Min(value = 1, message = "Quantity cant be less than 1") Integer totalQuantity,
        @NotNull(message = "Item id is required") UUID itemTypeId,
        @NotNull(message = "Item status is required") ItemStatus itemStatus,
        @NotNull(message = "Item condition is required") ItemCondition itemCondition
) {
}
