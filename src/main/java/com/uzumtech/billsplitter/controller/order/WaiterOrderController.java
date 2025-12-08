package com.uzumtech.billsplitter.controller.order;


import com.uzumtech.billsplitter.dto.request.order.GuestAddRequest;
import com.uzumtech.billsplitter.dto.request.order.MealAddRequest;
import com.uzumtech.billsplitter.dto.request.order.OrderCreateRequest;
import com.uzumtech.billsplitter.dto.response.order.OrderDetailResponse;
import com.uzumtech.billsplitter.dto.response.order.OrderResponse;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.service.order.OrderCreateService;
import com.uzumtech.billsplitter.service.order.OrderEditService;
import com.uzumtech.billsplitter.service.order.OrderGetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/waiter/orders")
public class WaiterOrderController {
    private final OrderCreateService orderCreateService;
    private final OrderEditService orderEditService;
    private final OrderGetService orderGetService;

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> list(
        @RequestParam(defaultValue = "0") final Integer page,
        @RequestParam(defaultValue = "10") final Integer pageSize,
        @AuthenticationPrincipal final WaiterEntity waiter) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<OrderResponse> orders = orderGetService.list(waiter, pageable);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> one(
        @PathVariable final Long id,
        @AuthenticationPrincipal final WaiterEntity waiter) {

        OrderDetailResponse detailResponse = orderGetService.getOne(id, waiter);

        return ResponseEntity.ok(detailResponse);
    }


    @PostMapping
    public ResponseEntity<OrderResponse> create(
        @Valid @RequestBody final OrderCreateRequest request,
        @AuthenticationPrincipal final WaiterEntity waiter) {

        OrderResponse response = orderCreateService.create(request, waiter);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/guests")
    public ResponseEntity<Void> addGuest(
        @PathVariable final Long orderId,
        @Valid @RequestBody final GuestAddRequest request,
        @AuthenticationPrincipal final WaiterEntity waiter) {

        orderEditService.addGuest(orderId, request, waiter);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/meals")
    public ResponseEntity<Void> addMealToGuest(
        @PathVariable final Long orderId,
        @Valid @RequestBody final MealAddRequest request,
        @AuthenticationPrincipal final WaiterEntity waiter) {

        orderEditService.addMealToGuest(orderId, request, waiter);

        return ResponseEntity.noContent().build();
    }
}
