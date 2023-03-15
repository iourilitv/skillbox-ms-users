package com.example.microservices.users.error.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorId {
    public static final ErrorId VALIDATION_ERROR = new ErrorId("validationError", "validationErrorInnerCode");

    private final String outerCode;
    private final String innerCode;

    public ErrorId(String outerCode, String innerCode) {
        this.outerCode = outerCode;
        this.innerCode = innerCode;
    }
}
