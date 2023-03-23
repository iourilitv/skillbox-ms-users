package com.example.microservices.users.error.exception;

public class FollowNotFoundResponseStatusException extends ResourceNotFoundResponseStatusException {

    private static final String RESOURCE_NAME = "Follow";
    private static final String ID_FIELD_NAME = "id";

    public FollowNotFoundResponseStatusException(long id) {
        super(RESOURCE_NAME, ID_FIELD_NAME, String.valueOf(id));
    }

    public FollowNotFoundResponseStatusException(String fieldName, String fieldValue) {
        super(RESOURCE_NAME, fieldName, fieldValue);
    }
}
