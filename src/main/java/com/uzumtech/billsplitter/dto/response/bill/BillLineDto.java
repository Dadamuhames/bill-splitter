package com.uzumtech.billsplitter.dto.response.bill;

import java.math.BigDecimal;

public record BillLineDto(
    Long mealId,
    String mealName,
    BigDecimal mealPrice,
    Integer quantity,
    BigDecimal subtotal
) {}
