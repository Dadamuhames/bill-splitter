package com.uzumtech.billsplitter.dto.response.bill;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record GuestBillDto(Long id, List<BillLineDto> details, BigDecimal commission, BigDecimal total, OffsetDateTime date) {}
