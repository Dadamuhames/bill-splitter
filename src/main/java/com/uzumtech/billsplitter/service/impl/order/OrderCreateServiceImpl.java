package com.uzumtech.billsplitter.service.impl.order;


import com.uzumtech.billsplitter.constant.enums.OrderStatus;
import com.uzumtech.billsplitter.dto.request.order.OrderCreateRequest;
import com.uzumtech.billsplitter.dto.request.order.OrderMealDto;
import com.uzumtech.billsplitter.dto.response.order.OrderResponse;
import com.uzumtech.billsplitter.entity.MealEntity;
import com.uzumtech.billsplitter.entity.TableEntity;
import com.uzumtech.billsplitter.entity.order.GuestEntity;
import com.uzumtech.billsplitter.entity.order.OrderEntity;
import com.uzumtech.billsplitter.entity.order.OrderItemEntity;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.mapper.OrderMapper;
import com.uzumtech.billsplitter.repository.GuestRepository;
import com.uzumtech.billsplitter.repository.OrderItemRepository;
import com.uzumtech.billsplitter.repository.OrderRepository;
import com.uzumtech.billsplitter.service.order.OrderCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class OrderCreateServiceImpl implements OrderCreateService {
    private final OrderRepository orderRepository;
    private final TableService tableService;
    private final OrderMealHelperService orderMealHelperService;
    private final OrderMapper orderMapper;
    private final GuestRepository guestRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderHelperService orderHelperService;

    @Transactional
    public OrderResponse create(final OrderCreateRequest request, final WaiterEntity waiter) {
        TableEntity table = tableService.getTableIfFree(request.tableId(), waiter);

        OrderEntity order = OrderEntity.builder()
            .status(OrderStatus.OPEN)
            .waiter(waiter)
            .table(table)
            .build();

        order = orderRepository.saveAndFlush(order);

        Map<Long, MealEntity> meals = orderMealHelperService.fetchRelevantMeals(request, waiter.getMerchant().getId());

        List<OrderItemEntity> orderItemEntities = collectAllGuestMeals(order, request, meals);

        if (request.sharedMeals() != null) {
            orderItemEntities.addAll(collectSharedMeals(order, request.sharedMeals(), meals));
        }

        orderItemRepository.saveAll(orderItemEntities);

        BigDecimal totalPrice = orderHelperService.calculateTotalPrice(orderItemEntities);

        return orderMapper.entityToResponse(order, table, totalPrice);
    }

    private static <T> Predicate<T> distinctById(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    private List<OrderItemEntity> collectAllGuestMeals(final OrderEntity order, final OrderCreateRequest request, final Map<Long, MealEntity> meals) {
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (var guest : request.guests()) {
            GuestEntity guestEntity = GuestEntity.builder().order(order).build();

            guestEntity = guestRepository.saveAndFlush(guestEntity);

            List<OrderMealDto> guestMeals = guest.meals().stream().filter(distinctById(OrderMealDto::mealId)).toList();

            for (var selectedMeal : guestMeals) {
                var meal = meals.get(selectedMeal.mealId());

                var orderItem = OrderItemEntity.builder()
                    .order(order)
                    .meal(meal)
                    .guest(guestEntity)
                    .mealPrice(meal.getPrice())
                    .quantity(selectedMeal.quantity())
                    .build();

                orderItems.add(orderItem);
            }
        }

        return orderItems;
    }


    private List<OrderItemEntity> collectSharedMeals(final OrderEntity order, final List<OrderMealDto> sharedMeals, final Map<Long, MealEntity> meals) {
        List<OrderItemEntity> orderItems = new ArrayList<>();

        List<OrderMealDto> distinctSharedMeals = sharedMeals.stream().filter(distinctById(OrderMealDto::mealId)).toList();

        for (var sharedMeal : distinctSharedMeals) {
            var meal = meals.get(sharedMeal.mealId());

            var orderItem = OrderItemEntity.builder()
                .order(order)
                .meal(meal)
                .mealPrice(meal.getPrice())
                .quantity(sharedMeal.quantity())
                .build();

            orderItems.add(orderItem);
        }

        return orderItems;
    }
}
