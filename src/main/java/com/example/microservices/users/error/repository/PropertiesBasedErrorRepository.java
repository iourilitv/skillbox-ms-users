package com.example.microservices.users.error.repository;

public interface PropertiesBasedErrorRepository {

    String getErrorMessage(String code);
}
