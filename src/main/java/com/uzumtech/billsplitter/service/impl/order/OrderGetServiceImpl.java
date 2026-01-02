package com.uzumtech.billsplitter.service.impl.order;


import com.uzumtech.billsplitter.dto.response.order.GuestResponse;
import com.uzumtech.billsplitter.dto.response.order.OrderDetailResponse;
import com.uzumtech.billsplitter.dto.response.order.OrderItemDto;
import com.uzumtech.billsplitter.dto.response.order.OrderResponse;
import com.uzumtech.billsplitter.entity.order.OrderEntity;
import com.uzumtech.billsplitter.entity.order.OrderItemEntity;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.mapper.OrderMapper;
import com.uzumtech.billsplitter.repository.OrderItemRepository;
import com.uzumtech.billsplitter.repository.OrderRepository;
import com.uzumtech.billsplitter.repository.projection.OrderProjection;
import com.uzumtech.billsplitter.service.order.OrderGetService;
import com.uzumtech.billsplitter.utils.CurrencyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderGetServiceImpl implements OrderGetService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CurrencyConverter currencyConverter;
    private final OrderHelperService orderHelperService;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public Page<OrderResponse> list(final WaiterEntity waiter, final Pageable pageable) {
        Page<OrderProjection> orders = orderRepository.findAllByWaiterWithPrice(waiter.getId(), pageable);

        return orders.map(orderMapper::projectionToResponse);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOne(final Long id, final WaiterEntity waiter) {
        OrderEntity order = orderHelperService.getInstance(id, waiter.getId());

        List<OrderItemEntity> orderItems = orderItemRepository.findAllByOrderId(order.getId());

        Map<Long, List<OrderItemEntity>> itemsByGuest = new HashMap<>();
        List<OrderItemDto> sharedMeals = new ArrayList<>();

        for (var orderItem : orderItems) {
            if (orderItem.getGuest() != null) {
                Long guestId = orderItem.getGuest().getId();
                itemsByGuest.computeIfAbsent(guestId, k -> new ArrayList<>()).add(orderItem);
            } else {
                sharedMeals.add(orderMapper.orderItemToDto(orderItem));
            }
        }

        List<GuestResponse> guestList = mapGuestMeals(itemsByGuest);

        BigDecimal totalPrice = orderHelperService.calculateTotalPrice(orderItems);

        return orderMapper.entityToDetailResponse(order, totalPrice, guestList, sharedMeals);
    }

    private List<GuestResponse> mapGuestMeals(final Map<Long, List<OrderItemEntity>> itemsByGuest) {
        List<GuestResponse> guestList = new ArrayList<>();

        for (Map.Entry<Long, List<OrderItemEntity>> entry : itemsByGuest.entrySet()) {
            Long guestId = entry.getKey();
            List<OrderItemEntity> guestOrderItems = entry.getValue();

            List<OrderItemDto> meals = new ArrayList<>();
            BigDecimal totalAmountToPay = BigDecimal.ZERO;

            for (OrderItemEntity item : guestOrderItems) {
                meals.add(orderMapper.orderItemToDto(item));

                BigDecimal priceUzs = currencyConverter.tiyinToUzs(item.getMealPrice());

                BigDecimal itemTotal = priceUzs.multiply(BigDecimal.valueOf(item.getQuantity()));

                totalAmountToPay = totalAmountToPay.add(itemTotal);
            }

            guestList.add(new GuestResponse(guestId, meals, totalAmountToPay));
        }

        return guestList;
    }
}
