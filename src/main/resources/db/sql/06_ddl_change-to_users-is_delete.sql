ALTER TABLE users_scheme.users
    ADD COLUMN is_deleted boolean NOT NULL DEFAULT False;

COMMENT ON COLUMN users_scheme.users.is_deleted
    IS 'For Soft Delete pattern';


ALTER TABLE users_scheme.users DROP COLUMN deleted_at;