package com.uzumtech.billsplitter.dto.response.order;

import java.math.BigDecimal;
import java.util.List;

public record GuestResponse(Long id, List<OrderItemDto> meals, BigDecimal totalAmountToPay) {
}
