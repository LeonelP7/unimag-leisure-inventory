package com.unimag.unimagleisureinventory.dtos.checkout;

import com.unimag.unimagleisureinventory.model.enums.ItemCondition;
import jakarta.validation.constraints.NotNull;

public record CheckInRequestDTO(
        @NotNull(message = "Item condition is required") ItemCondition returnedItemCondition
) {
}
