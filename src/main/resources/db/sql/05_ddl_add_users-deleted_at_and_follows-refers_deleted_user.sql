ALTER TABLE users_scheme.users
    ADD COLUMN deleted_at timestamp;


ALTER TABLE users_scheme.follows
    ADD COLUMN refers_deleted_user boolean NOT NULL DEFAULT False;