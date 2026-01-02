package com.uzumtech.billsplitter.repository.projection;

import java.time.OffsetDateTime;

public interface OrderProjection {
    Long getId();

    Long getTableId();

    String getTableNumber();

    Long getPrice();

    OffsetDateTime getCreatedAt();
}
