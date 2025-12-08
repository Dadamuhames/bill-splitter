package com.uzumtech.billsplitter.exception;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.ErrorType;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends ApplicationException {
    public OrderNotFoundException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(code, message, errorType, status);
    }

    public OrderNotFoundException(Error error) {
        super(error.getCode(), error.getMessage(), ErrorType.VALIDATION, HttpStatus.NOT_FOUND);
    }
}
