package com.example.microservices.users.error.exception;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

public class PreconditionFailedResponseStatusException extends BusinessResponseStatusException {

    public PreconditionFailedResponseStatusException(String reason) {
        super(PRECONDITION_FAILED, reason);
    }
}
