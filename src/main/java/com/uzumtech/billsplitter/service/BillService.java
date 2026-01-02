package com.uzumtech.billsplitter.service;

import com.uzumtech.billsplitter.dto.response.bill.BillResponse;
import com.uzumtech.billsplitter.entity.user.WaiterEntity;

public interface BillService {
    BillResponse splitBills(final Long orderId, final WaiterEntity waiter);
}
