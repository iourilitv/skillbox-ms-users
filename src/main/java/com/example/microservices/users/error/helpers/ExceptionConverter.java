package com.example.microservices.users.error.helpers;

import com.example.microservices.users.error.entity.ErrorId;
import com.example.microservices.users.error.entity.ErrorList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.example.microservices.users.error.entity.ErrorId.INTERNAL_ERROR;
import static com.example.microservices.users.error.entity.ErrorId.VALIDATION_ERROR;

@RequiredArgsConstructor
public class ExceptionConverter {

    private final ErrorBuilder errorBuilder;

    public ErrorList mapException(HttpStatus httpStatus, Exception ex) {
        ErrorId errorId = determineErrorId(httpStatus);
        return mapException(errorId, httpStatus, ex);
    }

    public ErrorList mapException(ErrorId errorId, HttpStatus httpStatus, Exception ex) {
        return new ErrorList(
                errorBuilder.buildError(errorId, httpStatus, ex)
        );
    }

    private ErrorId determineErrorId(HttpStatus httpStatus) {
        if (httpStatus.is4xxClientError()) {
            return VALIDATION_ERROR;
        }
        return INTERNAL_ERROR;
    }
}
