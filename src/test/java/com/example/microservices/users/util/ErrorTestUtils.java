package com.example.microservices.users.util;

import com.example.microservices.users.error.entity.Error;
import com.example.microservices.users.error.entity.ErrorId;
import com.example.microservices.users.error.entity.ErrorMeta;
import org.springframework.http.HttpStatus;

public class ErrorTestUtils {

    public static Error buildError(ErrorId errorId, HttpStatus httpStatus, String jExceptionMsg) {
        String messageToCustomer = determineMessageToCustomer(errorId.getInnerCode());
        int httpStatusCode = httpStatus.value();
        ErrorMeta getMetaIfEnabledOrDefault = new ErrorMeta().setJExceptionMsg(jExceptionMsg);
        return new Error()
                .setHttpStatusCode(httpStatusCode)
                .setFrontendCode(errorId.getOuterCode())
                .setMessageToCustomer(messageToCustomer)
                .setMeta(getMetaIfEnabledOrDefault);
    }

    private static String determineMessageToCustomer(String errorIdInnerCode) {
        if (ErrorId.BUSINESS_ERROR.getInnerCode().equals(errorIdInnerCode)) {
            return "Внутренняя бизнес ошибка";
        }
        return "Ошибка обработки данных";
    }

    private ErrorTestUtils() {
        throw new RuntimeException("Utility class");
    }
}
