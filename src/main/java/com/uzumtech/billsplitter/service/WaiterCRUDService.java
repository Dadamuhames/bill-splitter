package com.uzumtech.billsplitter.service;

import com.uzumtech.billsplitter.dto.request.waiter.WaiterCreateRequest;
import com.uzumtech.billsplitter.dto.request.waiter.WaiterUpdateRequest;
import com.uzumtech.billsplitter.dto.response.WaiterResponse;
import com.uzumtech.billsplitter.entity.user.MerchantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WaiterCRUDService {
    Page<WaiterResponse> list(final MerchantEntity merchant, final Pageable pageable);

    WaiterResponse create(final WaiterCreateRequest request, final MerchantEntity merchant);

    WaiterResponse update(final Long id, final WaiterUpdateRequest request, final MerchantEntity merchant);
}
