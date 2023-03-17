package com.example.microservices.users.error.helpers;

import com.example.microservices.users.error.entity.Error;
import com.example.microservices.users.error.entity.ErrorId;
import com.example.microservices.users.error.entity.ErrorMeta;
import com.example.microservices.users.error.repository.PropertiesBasedErrorRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * A guy who gets the data from the repo and transforms into Error domains
 */
@Getter
@Setter
public class ErrorBuilder {

    @Value("${errors.repositories.properties.errors.unknownErrorInnerCode:Ошибка обработки данных}")
    private String defaultMessageToCustomer;

    @Value("${errors.metaEnabled:true}")
    private boolean metaEnabled;

    @Autowired(required = false)
    private final List<PropertiesBasedErrorRepository> errorRepositories = new ArrayList<>();

    public Error buildError(ErrorId errorId, HttpStatus httpStatus, Exception ex) {
        String messageToCustomer = determineMessageToCustomer(errorId.getInnerCode());
        int httpStatusCode = determineHttpStatusCode(httpStatus);
        ErrorMeta getMetaIfEnabledOrDefault = metaEnabled ? ErrorMeta.fromException(ex) :
                new ErrorMeta().setJExceptionMsg("Error Metadata is disabled");
        return new Error()
                .setHttpStatusCode(httpStatusCode)
                .setFrontendCode(errorId.getOuterCode())
                .setMessageToCustomer(messageToCustomer)
                .setMeta(getMetaIfEnabledOrDefault);
    }

    private int determineHttpStatusCode(HttpStatus httpStatus) {
        return httpStatus.is4xxClientError() ? httpStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    /**
     * Fetches error title from some repository (possibly external service)
     * by {@param innerCode}.
     * <p>
     * If absent - use {@link #defaultMessageToCustomer}
     */
    private String determineMessageToCustomer(String innerCode) {
        return errorRepositories.stream()
                .map(repo -> repo.getErrorMessage(innerCode))
                .filter(message -> !isBlank(message))
                .findFirst()
                .orElse(defaultMessageToCustomer);
    }
}
