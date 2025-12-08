package com.uzumtech.billsplitter.constant.enums;

import lombok.Getter;

@Getter
public enum Error {
    INTERNAL_SERVICE_ERROR_CODE(10001, "System not available"),
    EXTERNAL_SERVICE_FAILED_ERROR_CODE(10002, "External service not available"),
    HANDLER_NOT_FOUND_ERROR_CODE(10003, "Handler not found"),
    JSON_NOT_VALID_ERROR_CODE(10004, "Json not valid"),
    VALIDATION_ERROR_CODE(10005, "Validation error"),
    INVALID_REQUEST_PARAM_ERROR_CODE(10006, "Invalid request param"),
    INTERNAL_TIMEOUT_ERROR_CODE(10007, "Internal timeout"),
    METHOD_NOT_SUPPORTED_ERROR_CODE(10008, "Method not supported"),
    MISSING_REQUEST_HEADER_ERROR_CODE(10009, "Missing request header"),
    LOGIN_INVALID_CODE(10010, "Login invalid"),
    ROLE_NOT_SUPPORTED_CODE(10011, "Provided Role not supported by the system"),
    JWT_INVALID_CODE(10012, "JWT invalid"),
    LOGIN_OR_TAX_NUMBER_EXISTS_CODE(10013, "Login or tax number already in use"),
    REFRESH_TOKEN_INVALID_CODE(10014, "Refresh invalid or expired"),
    PASSWORD_INVALID_CODE(10015, "Password invalid"),
    ORDER_NOT_FOUND_CODE(10016, "Order not found"),
    TABLE_NOT_AVAILABLE_CODE(10017, "Table not available"),
    INVALID_MEAL_ID_PROVIDED_CODE(10018, "Invalid meal id provided in the list"),
    MEAL_NOT_FOUND_CODE(10019, "Meal not found"),
    GUEST_NOT_FOUND_CODE(10020, "Guest not found"),
    ORDER_HAS_NO_GUESTS_CODE(10021, "Order has not guests"),
    WAITER_NOT_FOUND_CODE(10022, "Waiter not found"),
    WAITER_LOGIN_EXISTS_CODE(10023, "Waiter with that login exists");

    final int code;
    final String message;

    Error(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
