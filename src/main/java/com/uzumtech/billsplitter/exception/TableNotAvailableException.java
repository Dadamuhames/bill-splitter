package com.uzumtech.billsplitter.exception;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.ErrorType;
import org.springframework.http.HttpStatus;

public class TableNotAvailableException extends ApplicationException {
    public TableNotAvailableException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(code, message, errorType, status);
    }

    public TableNotAvailableException(Error error) {
        super(error.getCode(), error.getMessage(), ErrorType.VALIDATION, HttpStatus.BAD_REQUEST);
    }
}
