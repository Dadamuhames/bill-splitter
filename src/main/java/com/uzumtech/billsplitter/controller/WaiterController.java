package com.uzumtech.billsplitter.controller;

import com.uzumtech.billsplitter.dto.request.waiter.WaiterCreateRequest;
import com.uzumtech.billsplitter.dto.request.waiter.WaiterUpdateRequest;
import com.uzumtech.billsplitter.dto.response.WaiterResponse;
import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import com.uzumtech.billsplitter.service.impl.WaiterCRUDService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/merchant/waiters")
@RequiredArgsConstructor
public class WaiterController {
    private final WaiterCRUDService waiterCRUDService;

    @GetMapping
    public ResponseEntity<Page<WaiterResponse>> listWaiters(@RequestParam(defaultValue = "0") final Integer page, @RequestParam(defaultValue = "10") final Integer pageSize, @AuthenticationPrincipal final MerchantEntity merchant) {
        Pageable pageable = PageRequest.of(page, pageSize);

        var response = waiterCRUDService.list(merchant, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<WaiterResponse> createWaiter(@AuthenticationPrincipal MerchantEntity merchant, @Valid @RequestBody WaiterCreateRequest request) {
        var response = waiterCRUDService.create(request, merchant);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaiterResponse> updateWaiter(@PathVariable Long id, @AuthenticationPrincipal MerchantEntity merchant, @Valid @RequestBody WaiterUpdateRequest request) {
        var response = waiterCRUDService.update(id, request, merchant);

        return ResponseEntity.ok(response);
    }
}
