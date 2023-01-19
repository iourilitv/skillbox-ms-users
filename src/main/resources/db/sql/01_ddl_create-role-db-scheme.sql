CREATE ROLE microuser WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  CREATEDB
  NOCREATEROLE
  NOREPLICATION
  ENCRYPTED PASSWORD 'md5d8cfff6d8712c20b5350dcf0bd494db0';
COMMENT ON ROLE microuser IS 'User for Skillbox Microcervices course';


CREATE DATABASE users
    WITH
    OWNER = microuser
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
COMMENT ON DATABASE users
    IS 'db for Microcervises course';


CREATE SCHEMA users_scheme
    AUTHORIZATION microuser;
COMMENT ON SCHEMA users_scheme
    IS 'for Stage 1';
