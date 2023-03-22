package com.example.microservices.users.error;

import com.example.microservices.users.error.entity.Error;
import com.example.microservices.users.error.entity.ErrorId;
import com.example.microservices.users.error.entity.ErrorList;
import com.example.microservices.users.error.entity.ErrorMeta;
import com.example.microservices.users.error.exception.BaseResponseStatusException;
import com.example.microservices.users.error.exception.UserNotFoundResponseStatusException;
import com.example.microservices.users.error.helpers.ApplicationExceptionHandler;
import com.example.microservices.users.error.helpers.ErrorBuilder;
import com.example.microservices.users.error.helpers.ExceptionConverter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;

import static com.example.microservices.users.error.entity.ErrorId.BUSINESS_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@TestMethodOrder(value = MethodOrderer.MethodName.class)
class ApplicationExceptionHandlerTest {

    private final ErrorBuilder errorBuilder = mock(ErrorBuilder.class);
    private final ExceptionConverter exceptionConverter = mock(ExceptionConverter.class);
    private final ApplicationExceptionHandler exceptionHandler = new ApplicationExceptionHandler(exceptionConverter);

    @Test
    void test1_given_UserNotFoundResponseStatusException_thenCorrect_mapException() {
        long notExistId = 9999L;
        String resource = "User";
        String jExceptionMsg = String.format(
                "com.example.microservices.users.error.exception.%sNotFoundResponseStatusException{HttpStatus: 404 NOT_FOUND, Reason: Resource %s(id: %d) Not Found}",
                resource, resource, notExistId);
        var exception = new UserNotFoundResponseStatusException(999);
        HttpStatus expectedHttpStatus = INTERNAL_SERVER_ERROR;
        ErrorList errorList = new ErrorList(buildError(BUSINESS_ERROR, expectedHttpStatus, jExceptionMsg));
        try {
            throw exception;
        } catch (BaseResponseStatusException ex) {
            when(exceptionConverter.mapException(ex)).thenReturn(errorList);
            var response = exceptionHandler.handleException(ex);
            assertEquals(expectedHttpStatus, response.getStatusCode());
            assertEquals(errorList, response.getBody());
        }
    }

    private Error buildError(ErrorId errorId, HttpStatus httpStatus, String jExceptionMsg) {
        String messageToCustomer = "Внутренняя бизнес ошибка";
        int httpStatusCode = httpStatus.value();
        ErrorMeta getMetaIfEnabledOrDefault = new ErrorMeta().setJExceptionMsg(jExceptionMsg);
        return new Error()
                .setHttpStatusCode(httpStatusCode)
                .setFrontendCode(errorId.getOuterCode())
                .setMessageToCustomer(messageToCustomer)
                .setMeta(getMetaIfEnabledOrDefault);
    }
}