package com.uzumtech.billsplitter.dto.response.order;

import com.uzumtech.billsplitter.dto.response.MealResponse;

public record OrderItemDto(Long id, MealResponse meal, Integer quantity) {}
