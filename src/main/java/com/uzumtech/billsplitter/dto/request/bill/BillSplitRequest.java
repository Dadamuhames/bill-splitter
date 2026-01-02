package com.uzumtech.billsplitter.dto.request.bill;

import jakarta.validation.constraints.NotNull;

public record BillSplitRequest(@NotNull(message = "orderId required") Long orderId) {
}
