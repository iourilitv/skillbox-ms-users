package com.example.microservices.users.error.helpers;

import com.example.microservices.users.error.entity.ErrorId;
import com.example.microservices.users.error.entity.ErrorList;
import com.example.microservices.users.error.entity.ErrorMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import static com.example.microservices.users.error.entity.ErrorId.INTERNAL_ERROR;
import static com.example.microservices.users.error.entity.ErrorId.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
public class ExceptionConverter {

    private final ErrorBuilder errorBuilder;

    //TODO Fill UP other exceptions from the ResponseEntityExceptionHandler
    public ErrorList mapException(Exception ex) {
        if (ex instanceof HttpMediaTypeNotSupportedException
            || ex instanceof HttpMediaTypeNotAcceptableException) {
            return mapException(VALIDATION_ERROR, BAD_REQUEST, ex);
        }
        return mapException(INTERNAL_ERROR, INTERNAL_SERVER_ERROR, ex);
    }

    private ErrorList mapException(ErrorId errorId, HttpStatus httpStatus, Exception ex) {
        return new ErrorList(
                errorBuilder.forErrorId(errorId)
                        .httpStatus(httpStatus)
                        .detail(ex.getMessage())
                        .meta(ErrorMeta.fromException(ex))
                        .build()
        );
    }
}
