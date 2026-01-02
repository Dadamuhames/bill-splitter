package com.uzumtech.billsplitter.constant.enums;


import lombok.Getter;

@Getter
public enum Role {
    MERCHANT("ROLE_MERCHANT"),
    WAITER("ROLE_WAITER");

    final String role;

    Role(String role) {
        this.role = role;
    }
}
