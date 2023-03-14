package com.example.microservices.users.repository;

//TODO Replace with JpaRepository
public interface PropertiesBasedErrorRepository {

    String getErrorMessage(String code);
}
