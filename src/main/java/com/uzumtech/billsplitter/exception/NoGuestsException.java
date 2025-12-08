package com.uzumtech.billsplitter.exception;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.ErrorType;
import org.springframework.http.HttpStatus;

public class NoGuestsException extends ApplicationException {
    public NoGuestsException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(code, message, errorType, status);
    }

    public NoGuestsException(Error error) {
        super(error.getCode(), error.getMessage(), ErrorType.VALIDATION, HttpStatus.BAD_REQUEST);
    }
}
