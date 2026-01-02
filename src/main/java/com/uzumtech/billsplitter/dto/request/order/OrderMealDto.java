package com.uzumtech.billsplitter.dto.request.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderMealDto(@NotNull Long mealId, @NotNull @Positive Integer quantity) {}
