package com.uzumtech.billsplitter.dto.request.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GuestAddRequest(
    @NotNull(message = "meals required") @NotEmpty(message = "meals cannot be empty") @Valid List<OrderMealDto> meals) {
}
