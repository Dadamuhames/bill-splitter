package com.uzumtech.billsplitter.dto.response.order;

import com.uzumtech.billsplitter.dto.response.TableResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetailResponse(Long id,
                                  TableResponse table,
                                  BigDecimal price,
                                  List<GuestResponse> guests,
                                  List<OrderItemDto> sharedMeals) {
}
