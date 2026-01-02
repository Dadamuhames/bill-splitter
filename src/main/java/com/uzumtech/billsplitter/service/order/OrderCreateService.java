package com.uzumtech.billsplitter.service.order;

import com.uzumtech.billsplitter.dto.request.order.OrderCreateRequest;
import com.uzumtech.billsplitter.dto.response.order.OrderResponse;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;

public interface OrderCreateService {
    OrderResponse create(final OrderCreateRequest request, final WaiterEntity waiter);
}
