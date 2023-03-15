package com.example.microservices.users.error.repository;

//TODO Replace with JpaRepository
public interface PropertiesBasedErrorRepository {

    String getErrorMessage(String code);
}
