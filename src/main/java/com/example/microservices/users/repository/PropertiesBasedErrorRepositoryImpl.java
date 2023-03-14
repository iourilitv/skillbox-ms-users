package com.example.microservices.users.repository;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

//TODO Replace with JpaRepository
@Getter
@ConfigurationProperties("errors.repositories.properties")
public class PropertiesBasedErrorRepositoryImpl implements PropertiesBasedErrorRepository {

    private final Map<String, String> errors = new HashMap<>();
    private final Map<String, String> messages = new HashMap<>();

    @PostConstruct
    public void configure() {
        errors.forEach((k, v) -> messages.put(v, k));
    }

    @Override
    public String getErrorMessage(String code) {
        return errors.get(code);
    }
}
