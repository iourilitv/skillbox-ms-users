package com.example.microservices.users.error.exception;

public class UserNotFoundResponseStatusException extends ResourceNotFoundResponseStatusException {

    private static final String USER_RESOURCE_NAME = "User";
    private static final String ID_FIELD_NAME = "id";

    public UserNotFoundResponseStatusException(String fieldValue) {
        super(USER_RESOURCE_NAME, ID_FIELD_NAME, fieldValue);
    }

    public UserNotFoundResponseStatusException(String fieldName, String fieldValue) {
        super(USER_RESOURCE_NAME, fieldName, fieldValue);
    }
}
