package com.uzumtech.billsplitter.dto.response.bill;

import java.util.List;

public record BillResponse(List<GuestBillDto> bills) {}
