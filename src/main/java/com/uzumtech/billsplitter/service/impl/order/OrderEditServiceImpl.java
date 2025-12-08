package com.uzumtech.billsplitter.service.impl.order;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.dto.request.order.GuestAddRequest;
import com.uzumtech.billsplitter.dto.request.order.MealAddRequest;
import com.uzumtech.billsplitter.dto.request.order.OrderMealDto;
import com.uzumtech.billsplitter.entity.MealEntity;
import com.uzumtech.billsplitter.entity.order.GuestEntity;
import com.uzumtech.billsplitter.entity.order.OrderEntity;
import com.uzumtech.billsplitter.entity.order.OrderItemEntity;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.exception.GuestNotFoundException;
import com.uzumtech.billsplitter.repository.GuestRepository;
import com.uzumtech.billsplitter.repository.OrderItemRepository;
import com.uzumtech.billsplitter.service.order.OrderEditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderEditServiceImpl implements OrderEditService {
    private final OrderHelperService orderHelperService;
    private final OrderMealHelperService orderMealHelperService;
    private final OrderItemRepository orderItemRepository;
    private final GuestRepository guestRepository;

    @Transactional
    public void addGuest(final Long orderId, final GuestAddRequest request, final WaiterEntity waiter) {
        OrderEntity order = orderHelperService.getInstance(orderId, waiter.getId());

        GuestEntity guestEntity = GuestEntity.builder().order(order).build();

        guestEntity = guestRepository.saveAndFlush(guestEntity);

        storeGuestMeals(order, guestEntity, request.meals(), waiter.getMerchant().getId());
    }


    private void storeGuestMeals(final OrderEntity order, final GuestEntity guest, final List<OrderMealDto> orderMeals, final Long merchantId) {
        Map<Long, MealEntity> relevantMeals = orderMealHelperService.fetchRelevantMeals(orderMeals, merchantId);

        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (var orderMeal : orderMeals) {
            var meal = relevantMeals.get(orderMeal.mealId());

            var orderItem = OrderItemEntity.builder().guest(guest).order(order).meal(meal).mealPrice(meal.getPrice()).quantity(orderMeal.quantity()).build();

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);
    }


    @Transactional
    public void addMealToGuest(final Long orderId, final MealAddRequest request, final WaiterEntity waiter) {
        OrderEntity order = orderHelperService.getInstance(orderId, waiter.getId());

        MealEntity meal = orderMealHelperService.findByIdAndMerchantId(request.mealId(), waiter.getMerchant().getId());
        GuestEntity guest = guestRepository.findByIdAndOrderId(request.guestId(), orderId).orElseThrow(() -> new GuestNotFoundException(Error.GUEST_NOT_FOUND_CODE));

        OrderItemEntity orderItem = orderItemRepository.findByOrderIdAndGuestIdAndMealId(orderId, guest.getId(), meal.getId()).orElse(null);

        if (orderItem != null) {
            orderItem.setQuantity(orderItem.getQuantity() + request.quantity());
            orderItemRepository.save(orderItem);

        } else {
            var newOrderItem = OrderItemEntity.builder().meal(meal).guest(guest).order(order).quantity(request.quantity()).mealPrice(meal.getPrice()).build();

            orderItemRepository.save(newOrderItem);
        }
    }
}
