package com.uzumtech.billsplitter.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyConverter {
    public BigDecimal tiyinToUzs(final Long tiyin) {
        return BigDecimal.valueOf(tiyin / 100);
    }
}
