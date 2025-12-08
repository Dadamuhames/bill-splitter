package com.uzumtech.billsplitter.exception;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.ErrorType;
import org.springframework.http.HttpStatus;

public class RoleNotSupportedException extends ApplicationException {
    public RoleNotSupportedException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(code, message, errorType, status);
    }

    public RoleNotSupportedException(Error error) {
        super(error.getCode(), error.getMessage(), ErrorType.EXTERNAL, HttpStatus.BAD_REQUEST);
    }
}
