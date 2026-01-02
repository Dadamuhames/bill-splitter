package com.uzumtech.billsplitter.service.impl;


import com.uzumtech.billsplitter.entity.MerchantCommissionEntity;
import com.uzumtech.billsplitter.repository.MerchantCommissionRepository;
import com.uzumtech.billsplitter.service.CommissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CommissionServiceImpl implements CommissionService {
    private final MerchantCommissionRepository commissionRepository;

    public BigDecimal getMerchantCommissionRate(final Long merchantId) {
        MerchantCommissionEntity commissionEntity = commissionRepository.findCurrentCommission(merchantId).orElse(null);

        if (commissionEntity == null) return BigDecimal.ZERO;

        return commissionEntity.getCommissionRate();
    }
}
