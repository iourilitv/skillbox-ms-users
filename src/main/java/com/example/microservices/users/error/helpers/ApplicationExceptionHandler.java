package com.example.microservices.users.error.helpers;

import com.example.microservices.users.error.entity.ErrorList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import static com.example.microservices.users.error.helpers.ErrorMessage.HANDLING_ERROR_MESSAGE;
import static com.example.microservices.users.error.helpers.ErrorMessage.UNKNOWN_ERROR_MESSAGE;

@Slf4j
@Order(1)
@ControllerAdvice
@ConditionalOnBean(ExceptionConverter.class)
@RequiredArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private final ExceptionConverter exceptionConverter;

    @PostConstruct
    public void logConfig() {
        log.info("{} has been initialized", this.getClass().getSimpleName());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownException(Exception ex, HttpServletRequest request) {
        logError(UNKNOWN_ERROR_MESSAGE, ex);
        ErrorList errorList = exceptionConverter.mapException(ex);
        return toResponseEntity(errorList);
    }

    @Override
    protected @NonNull ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body,
                                                             @NonNull HttpHeaders headers, @NonNull HttpStatus status,
                                                             @NonNull WebRequest request) {
        logError(HANDLING_ERROR_MESSAGE, ex);
        ErrorList errorList = exceptionConverter.mapException(ex);
        return toResponseEntity(errorList);
    }

    private ResponseEntity<Object> toResponseEntity(ErrorList errorList) {
        return new ResponseEntity<>(
                errorList,
                HttpStatus.valueOf(errorList.getErrors().get(0).getHttpStatus())
        );
    }

    private void logError(String errorMessage, Exception ex) {
        if (log.isErrorEnabled()) {
            log.error(errorMessage + ": {}", ex.getMessage());
        }
    }
}
