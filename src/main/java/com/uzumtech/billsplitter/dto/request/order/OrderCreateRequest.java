package com.uzumtech.billsplitter.dto.request.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequest(@NotNull(message = "tableId required") Long tableId,
                                 @NotNull(message = "guests required") @NotEmpty(message = "guests cant be empty") @Valid List<OrderGuestDto> guests,
                                 List<OrderMealDto> sharedMeals) {

    public record OrderGuestDto(@NotNull @NotEmpty @Valid List<OrderMealDto> meals) {}
}
