package com.uzumtech.billsplitter.exception;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MerchantValidationException extends ApplicationException {


    public MerchantValidationException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(code, message, errorType, status);
    }

    public MerchantValidationException(Error error) {
        super(error.getCode(), error.getMessage(), ErrorType.VALIDATION, HttpStatus.BAD_REQUEST);
    }
}
