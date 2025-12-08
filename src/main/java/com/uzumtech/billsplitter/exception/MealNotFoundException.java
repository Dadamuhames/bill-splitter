package com.uzumtech.billsplitter.exception;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.ErrorType;
import org.springframework.http.HttpStatus;

public class MealNotFoundException extends ApplicationException {

    public MealNotFoundException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(code, message, errorType, status);
    }

    public MealNotFoundException(Error error) {
        super(error.getCode(), error.getMessage(), ErrorType.VALIDATION, HttpStatus.NOT_FOUND);
    }
}
