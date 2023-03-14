package com.example.microservices.users.exception.helpers;

import com.example.microservices.users.entity.ErrorId;
import com.example.microservices.users.entity.ErrorList;
import com.example.microservices.users.entity.ErrorMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public class ExceptionConverter {

    private final ErrorBuilder errorBuilder;

    //TODO Fill UP other exceptions from the ResponseEntityExceptionHandler
    public ErrorList mapException(Exception ex) {
        return mapException(ErrorId.VALIDATION_ERROR, BAD_REQUEST, ex);
    }

    private ErrorList mapException(ErrorId errorId, HttpStatus httpStatus, Exception ex) {
        return new ErrorList(
                errorBuilder.forErrorId(errorId)
                        .httpStatus(httpStatus)
                        .meta(ErrorMeta.fromException(ex))
                        .build()
        );
    }
}
