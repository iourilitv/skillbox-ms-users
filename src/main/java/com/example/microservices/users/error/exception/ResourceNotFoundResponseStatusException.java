package com.example.microservices.users.error.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundResponseStatusException extends BusinessResponseStatusException {

    protected final String resourceName;
    protected final String fieldName;
    protected final String fieldValue;

    public ResourceNotFoundResponseStatusException(String resourceName, String fieldName, String fieldValue) {
        super(HttpStatus.NOT_FOUND, String.format("Resource %s(%s: %s) Not Found", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
