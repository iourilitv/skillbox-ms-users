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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.PostConstruct;

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

    @Override
    protected @NonNull ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body,
                                                             @NonNull HttpHeaders headers, @NonNull HttpStatus status,
                                                             @NonNull WebRequest request) {
        log.error(ErrorMessage.UNKNOWN_ERROR_MESSAGE, ex);
        return toResponseEntity(exceptionConverter.mapException(ex));
    }

    private ResponseEntity<Object> toResponseEntity(ErrorList errorList) {
        return new ResponseEntity<>(
                errorList,
                HttpStatus.valueOf(errorList.getErrors().get(0).getHttpStatus())
        );
    }
}
