package com.example.microservices.users.error.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ErrorId implements Serializable {

    public static final ErrorId BUSINESS_ERROR = new ErrorId("businessError", "businessErrorInnerCode");
    public static final ErrorId INTERNAL_ERROR = new ErrorId("internalError", "internalErrorInnerCode");
    public static final ErrorId VALIDATION_ERROR = new ErrorId("validationError", "validationErrorInnerCode");
    public static final ErrorId UNKNOWN_ERROR = new ErrorId("unknownError", "unknownErrorInnerCode");

    private final String outerCode;
    private final String innerCode;

    /**
     * @param outerCode example: {@code businessError}
     * @param innerCode from Repository. example: {@code businessErrorInnerCode}
     */
    private ErrorId(String outerCode, String innerCode) {
        this.outerCode = outerCode;
        this.innerCode = innerCode;
    }
}
