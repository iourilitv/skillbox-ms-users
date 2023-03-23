package com.example.microservices.users.error.config;

import com.example.microservices.users.error.helpers.ErrorBuilder;
import com.example.microservices.users.error.helpers.ExceptionConverter;
import com.example.microservices.users.error.repository.PropertiesBasedErrorRepositoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "enabled", prefix = "errors.handling")
@EnableConfigurationProperties(PropertiesBasedErrorRepositoryImpl.class)
public class ErrorHandlingConfig {

    @Bean
    public ErrorBuilder errorBuilder() {
        return new ErrorBuilder();
    }

    @Bean
    public ExceptionConverter exceptionConverter(ErrorBuilder errorBuilder) {
        return new ExceptionConverter(errorBuilder);
    }
}
