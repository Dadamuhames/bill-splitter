package com.uzumtech.billsplitter.service.order;

import com.uzumtech.billsplitter.dto.response.order.OrderDetailResponse;
import com.uzumtech.billsplitter.dto.response.order.OrderResponse;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderGetService {
    Page<OrderResponse> list(final WaiterEntity waiter, final Pageable pageable);

    OrderDetailResponse getOne(final Long id, final WaiterEntity waiter);
}
