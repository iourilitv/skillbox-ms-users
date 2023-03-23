package com.example.microservices.users.error;

import com.example.microservices.users.error.entity.Error;
import com.example.microservices.users.error.entity.ErrorId;
import com.example.microservices.users.error.entity.ErrorList;
import com.example.microservices.users.error.exception.BaseResponseStatusException;
import com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException;
import com.example.microservices.users.error.helpers.ErrorBuilder;
import com.example.microservices.users.error.helpers.ExceptionConverter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeoutException;

import static com.example.microservices.users.error.entity.ErrorId.BUSINESS_ERROR;
import static com.example.microservices.users.error.entity.ErrorId.INTERNAL_ERROR;
import static com.example.microservices.users.error.entity.ErrorId.UNKNOWN_ERROR;
import static com.example.microservices.users.error.entity.ErrorId.VALIDATION_ERROR;
import static com.example.microservices.users.util.ErrorTestUtils.buildError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
class ExceptionConverterTest {

    private final ErrorBuilder errorBuilder = mock(ErrorBuilder.class);
    private final ExceptionConverter exceptionConverter = new ExceptionConverter(errorBuilder);

    @Test
    void test11_givenPreconditionFailedResponseStatusException_thenCorrect_mapException() {
        long sameId = 999L;
        String reason = String.format(
                "Following And Follower Must Be Not The Same Person But Got The Same followingId: %s and followerId: %s}",
                sameId, sameId);
        String jExceptionMsg = String.format(
                "com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException{HttpStatus: 412 PRECONDITION_FAILED, Reason: %s}",
                reason);
        HttpStatus httpStatus = INTERNAL_SERVER_ERROR;
        ErrorId errorId = BUSINESS_ERROR;
        Error error = buildError(errorId, httpStatus, jExceptionMsg);
        ErrorList expected = new ErrorList(error);
        try {
            throw new PreconditionFailedResponseStatusException(reason);
        } catch (BaseResponseStatusException ex) {
            when(errorBuilder.buildError(errorId, httpStatus, ex)).thenReturn(error);
            var actual = exceptionConverter.mapException(ex);
            assertEquals(expected, actual);
        }
    }

    @Test
    void test12_givenUnknownResponseStatusException_thenCorrect_mapException() {
        String reason = "Unknown ResponseStatusException";
        String jExceptionMsg = String.format(
                "com.example.microservices.users.error.exception.PreconditionFailedResponseStatusException{HttpStatus: 400 BAD_REQUEST, Reason: %s}",
                reason);
        HttpStatus httpStatus = INTERNAL_SERVER_ERROR;
        ErrorId errorId = UNKNOWN_ERROR;
        Error error = buildError(errorId, httpStatus, jExceptionMsg);
        ErrorList expected = new ErrorList(error);
        try {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
        } catch (ResponseStatusException ex) {
            when(errorBuilder.buildError(errorId, httpStatus, ex)).thenReturn(error);
            var actual = exceptionConverter.mapException(ex);
            assertEquals(expected, actual);
        }
    }

    @Test
    void test21_givenHttpStatusUNSUPPORTED_MEDIA_TYPE_thenCorrect_mapExceptionWithArgHttpStatus() {
        String message = "Content type 'text/plain;charset=UTF-8' not supported";
        String jExceptionMsg = String.format("org.springframework.web.HttpMediaTypeNotSupportedException: %s", message);
        HttpStatus httpStatus = UNSUPPORTED_MEDIA_TYPE;
        ErrorId errorId = VALIDATION_ERROR;
        Error error = buildError(errorId, httpStatus, jExceptionMsg);
        ErrorList expected = new ErrorList(error);
        try {
            throw new HttpMediaTypeNotSupportedException(message);
        } catch (HttpMediaTypeNotSupportedException ex) {
            when(errorBuilder.buildError(errorId, httpStatus, ex)).thenReturn(error);
            var actual = exceptionConverter.mapException(httpStatus, ex);
            assertEquals(expected, actual);
        }
    }

    @Test
    void test22_givenHttpStatusSERVICE_UNAVAILABLE_thenCorrect_mapExceptionWithArgHttpStatus() {
        String message = "Service unavailable";
        String jExceptionMsg = String.format("java.util.concurrent.TimeoutException: %s", message);
        HttpStatus httpStatus = SERVICE_UNAVAILABLE;
        ErrorId errorId = INTERNAL_ERROR;
        Error error = buildError(errorId, httpStatus, jExceptionMsg);
        ErrorList expected = new ErrorList(error);
        try {
            throw new TimeoutException(message);
        } catch (TimeoutException ex) {
            when(errorBuilder.buildError(errorId, httpStatus, ex)).thenReturn(error);
            var actual = exceptionConverter.mapException(httpStatus, ex);
            assertEquals(expected, actual);
        }
    }
}
