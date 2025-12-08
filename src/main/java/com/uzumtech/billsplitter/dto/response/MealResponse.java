package com.uzumtech.billsplitter.dto.response;

import java.math.BigDecimal;

public record MealResponse(Long id, String name, BigDecimal price) {
}
