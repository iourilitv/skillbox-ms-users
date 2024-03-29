package com.example.microservices.users.error;

import com.example.microservices.users.error.entity.ErrorList;
import com.example.microservices.users.error.entity.ErrorMeta;
import com.example.microservices.users.error.exception.BaseResponseStatusException;
import com.example.microservices.users.error.exception.FollowNotFoundResponseStatusException;
import com.example.microservices.users.error.exception.UserNotFoundResponseStatusException;
import com.example.microservices.users.error.helpers.ApplicationExceptionHandler;
import com.example.microservices.users.error.helpers.ExceptionConverter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;

import static com.example.microservices.users.error.entity.ErrorId.BUSINESS_ERROR;
import static com.example.microservices.users.util.ErrorTestUtils.buildError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
class ApplicationExceptionHandlerTest {

    private final ExceptionConverter exceptionConverter = mock(ExceptionConverter.class);
    private final ApplicationExceptionHandler exceptionHandler = new ApplicationExceptionHandler(exceptionConverter);

    @Test
    void test1_given_UserNotFoundResponseStatusException_thenCorrect_mapException() {
        long notExistId = 9999L;
        String resource = "User";
        String jExceptionMsg = String.format(
                "com.example.microservices.users.error.exception.%sNotFoundResponseStatusException{HttpStatus: 404 NOT_FOUND, Reason: Resource %s(id: %d) Not Found}",
                resource, resource, notExistId);
        HttpStatus expectedHttpStatus = INTERNAL_SERVER_ERROR;
        ErrorList expectedErrorList = new ErrorList(buildError(BUSINESS_ERROR, expectedHttpStatus, jExceptionMsg));
        var exception1 = new UserNotFoundResponseStatusException(notExistId);
        String fieldName = "id";
        var exception2 = new UserNotFoundResponseStatusException(fieldName, String.valueOf(notExistId));
        assertEquals(exception1, exception2);
        try {
            throw exception1;
        } catch (BaseResponseStatusException ex) {
            when(exceptionConverter.mapException(ex)).thenReturn(expectedErrorList);
            var actualResponseEntity = exceptionHandler.handleException(ex);
            assertEquals(expectedHttpStatus, actualResponseEntity.getStatusCode());
            assertEquals(expectedErrorList, actualResponseEntity.getBody());

            ErrorMeta expectedErrorMeta = new ErrorMeta().setJExceptionMsg(jExceptionMsg);
            ErrorMeta actualErrorMeta = ErrorMeta.fromException(ex);
            assertEquals(expectedErrorMeta, actualErrorMeta);
        }
    }

    @Test
    void test2_given_FollowNotFoundResponseStatusException_thenCorrect_mapException() {
        long notExistId = 9999L;
        String resource = "Follow";
        String jExceptionMsg = String.format(
                "com.example.microservices.users.error.exception.%sNotFoundResponseStatusException{HttpStatus: 404 NOT_FOUND, Reason: Resource %s(id: %d) Not Found}",
                resource, resource, notExistId);
        HttpStatus expectedHttpStatus = INTERNAL_SERVER_ERROR;
        ErrorList expectedErrorList = new ErrorList(buildError(BUSINESS_ERROR, expectedHttpStatus, jExceptionMsg));
        var exception1 = new FollowNotFoundResponseStatusException(notExistId);
        String fieldName = "id";
        var exception2 = new FollowNotFoundResponseStatusException(fieldName, String.valueOf(notExistId));
        assertEquals(exception1, exception2);
        try {
            throw exception1;
        } catch (BaseResponseStatusException ex) {
            when(exceptionConverter.mapException(ex)).thenReturn(expectedErrorList);
            var actualResponseEntity = exceptionHandler.handleException(ex);
            assertEquals(expectedHttpStatus, actualResponseEntity.getStatusCode());
            assertEquals(expectedErrorList, actualResponseEntity.getBody());
        }
    }
}