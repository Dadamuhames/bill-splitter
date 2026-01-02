package com.uzumtech.billsplitter.service.order;

import com.uzumtech.billsplitter.dto.request.order.GuestAddRequest;
import com.uzumtech.billsplitter.dto.request.order.MealAddRequest;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;

public interface OrderEditService {

    void addGuest(final Long orderId, final GuestAddRequest request, final WaiterEntity waiter);

    void addMealToGuest(final Long orderId, final MealAddRequest request, final WaiterEntity waiter);
}
