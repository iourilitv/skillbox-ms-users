package com.example.microservices.users.util;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

public class SimplePostgreSQLTestContainer {

    private SimplePostgreSQLTestContainer() {
    }

    static PostgreSQLContainer<?> sqlContainer = new PostgreSQLContainer<>("postgres:14.6")
            .withDatabaseName("simple_test_db")
            .withUsername("test")
            .withPassword("test");

    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    public static PostgreSQLContainer<?> sqlContainer() {
        return sqlContainer;
    }
}
