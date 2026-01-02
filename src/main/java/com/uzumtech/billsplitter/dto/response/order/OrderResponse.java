package com.uzumtech.billsplitter.dto.response.order;

import com.uzumtech.billsplitter.dto.response.TableResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record OrderResponse(Long id, TableResponse table, BigDecimal price, OffsetDateTime createdAt) {
}
