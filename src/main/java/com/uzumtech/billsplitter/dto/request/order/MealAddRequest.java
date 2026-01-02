package com.uzumtech.billsplitter.dto.request.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MealAddRequest(Long guestId, @NotNull(message = "mealId required") Long mealId,
                             @NotNull(message = "quantity required") @Positive Integer quantity) {
}
