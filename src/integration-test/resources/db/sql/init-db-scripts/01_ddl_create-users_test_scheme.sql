CREATE SCHEMA IF NOT EXISTS users_test_scheme
    AUTHORIZATION microuser;
COMMENT ON SCHEMA users_test_scheme
    IS 'A temporary schema of microcervice "users" for integration tests';
