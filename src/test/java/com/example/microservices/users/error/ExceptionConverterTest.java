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
import org.springframework.web.server.ResponseStatusException;

import static com.example.microservices.users.error.entity.ErrorId.BUSINESS_ERROR;
import static com.example.microservices.users.error.entity.ErrorId.UNKNOWN_ERROR;
import static com.example.microservices.users.util.ErrorTestUtils.buildError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
public class ExceptionConverterTest {

    private final ErrorBuilder errorBuilder = mock(ErrorBuilder.class);
    private final ExceptionConverter exceptionConverter = new ExceptionConverter(errorBuilder);

    @Test
    void test1_givenPreconditionFailedResponseStatusException_thenCorrect_mapException() {
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
    void test2_givenUnknownResponseStatusException_thenCorrect_mapException() {
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
}
