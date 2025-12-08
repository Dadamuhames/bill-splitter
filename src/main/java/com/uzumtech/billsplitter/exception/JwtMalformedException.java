package com.uzumtech.billsplitter.exception;

import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.ErrorType;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;

public class JwtMalformedException extends ApplicationException {

    public JwtMalformedException(int code, String message, ErrorType errorType, HttpStatus status) {
        super(code, message, errorType, status);
    }

    public JwtMalformedException(Error error, JwtException ex) {
        super(error.getCode(), ex.getMessage(), ErrorType.VALIDATION, HttpStatus.BAD_REQUEST);
    }

    public JwtMalformedException(Error error) {
        super(error.getCode(), error.getMessage(), ErrorType.VALIDATION, HttpStatus.BAD_REQUEST);
    }
}
