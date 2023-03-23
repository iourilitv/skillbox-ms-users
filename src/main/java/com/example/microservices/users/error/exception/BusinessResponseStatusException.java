package com.example.microservices.users.error.exception;

import org.springframework.http.HttpStatus;

import static com.example.microservices.users.error.entity.ErrorId.BUSINESS_ERROR;

public class BusinessResponseStatusException extends BaseResponseStatusException {

    public BusinessResponseStatusException(HttpStatus status, String reason) {
        super(status, reason, BUSINESS_ERROR);
    }
}
