package com.uzumtech.billsplitter.service;

import java.math.BigDecimal;

public interface CommissionService {
    BigDecimal getMerchantCommissionRate(final Long merchantId);
}
