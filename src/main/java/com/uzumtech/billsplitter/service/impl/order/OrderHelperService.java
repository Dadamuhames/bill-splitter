package com.uzumtech.billsplitter.service.impl.order;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.entity.order.OrderEntity;
import com.uzumtech.billsplitter.entity.order.OrderItemEntity;
import com.uzumtech.billsplitter.exception.OrderNotFoundException;
import com.uzumtech.billsplitter.repository.OrderRepository;
import com.uzumtech.billsplitter.utils.CurrencyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderHelperService {
    private final OrderRepository orderRepository;
    private final CurrencyConverter currencyConverter;

    public OrderEntity getInstance(final Long id, final Long waiterId) {
        return orderRepository.findByIdAndWaiterId(id, waiterId).orElseThrow(() -> new OrderNotFoundException(Error.ORDER_NOT_FOUND_CODE));
    }

    public BigDecimal calculateTotalPrice(final List<OrderItemEntity> orderItems) {
        return orderItems.stream().map(item -> currencyConverter.tiyinToUzs(item.getMealPrice()).multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
