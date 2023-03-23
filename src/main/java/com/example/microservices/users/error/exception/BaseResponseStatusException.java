package com.example.microservices.users.error.exception;

import com.example.microservices.users.error.entity.ErrorId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@EqualsAndHashCode(callSuper = false)
public class BaseResponseStatusException extends ResponseStatusException {

    protected final ErrorId errorId;

    public BaseResponseStatusException(HttpStatus status, String reason, ErrorId errorId) {
        super(status, reason);
        this.errorId = errorId;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "{" +
                "HttpStatus: " + this.getStatus() +
                ", Reason: " + this.getReason() +
                "}";
    }
}
