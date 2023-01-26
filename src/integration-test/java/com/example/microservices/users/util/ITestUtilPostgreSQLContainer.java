package com.example.microservices.users.util;

import org.apache.commons.lang3.tuple.Pair;
import org.testcontainers.containers.PostgreSQLContainer;

public class ITestUtilPostgreSQLContainer extends PostgreSQLContainer<ITestUtilPostgreSQLContainer> {
    private static final String DOCKER_IMAGE_NAME = "postgres:14.6";
    private static final String DATABASE_NAME = "users";
    private static final String USER_NAME = "microuser";
    private static final String USER_PASSWORD = "microuser";
    private static final Pair<String, String> CURRENT_SCHEMA = Pair.of("currentSchema", "users_test_scheme");
    private static final String INIT_SCRIPT_FILE = "db/sql/init-db-scripts/01_ddl_create-users_test_scheme.sql";
    private static ITestUtilPostgreSQLContainer container;

    private ITestUtilPostgreSQLContainer() {
        super(DOCKER_IMAGE_NAME);
        super.withDatabaseName(DATABASE_NAME);
        super.withUsername(USER_NAME);
        super.withPassword(USER_PASSWORD);
        //Customizing db schema instead of a default schema, public, but it is not required
        super.withUrlParam(CURRENT_SCHEMA.getKey(), CURRENT_SCHEMA.getValue());
        super.withInitScript(INIT_SCRIPT_FILE);
    }

    public static ITestUtilPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new ITestUtilPostgreSQLContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("POSTGRES_DB_URL", container.getJdbcUrl());
        System.setProperty("POSTGRES_DB_USER", container.getUsername());
        System.setProperty("POSTGRES_DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
        // It allows using one sql container for all ITest classes
    }
}
