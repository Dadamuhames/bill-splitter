package com.uzumtech.billsplitter.controller.order;


import com.uzumtech.billsplitter.dto.request.bill.BillSplitRequest;
import com.uzumtech.billsplitter.dto.response.bill.BillResponse;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;
import com.uzumtech.billsplitter.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/waiter/bill-splitter")
public class BillSplitterController {
    private final BillService billService;

    @PostMapping
    public ResponseEntity<BillResponse> splitBill(@Valid @RequestBody BillSplitRequest request, @AuthenticationPrincipal final WaiterEntity waiter) {
        BillResponse response = billService.splitBills(request.orderId(), waiter);

        return ResponseEntity.ok(response);
    }
}
