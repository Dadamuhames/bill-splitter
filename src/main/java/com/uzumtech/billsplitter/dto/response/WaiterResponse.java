package com.uzumtech.billsplitter.dto.response;

import java.time.OffsetDateTime;

public record WaiterResponse(Long id, String fullName, String login, OffsetDateTime createdAt) {
}
