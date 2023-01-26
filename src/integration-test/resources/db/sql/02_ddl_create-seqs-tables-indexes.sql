CREATE SEQUENCE users_test_scheme.cities_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;
ALTER SEQUENCE users_test_scheme.cities_id_seq
    OWNER TO microuser;


CREATE SEQUENCE users_test_scheme.follows_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
ALTER SEQUENCE users_test_scheme.follows_id_seq
    OWNER TO microuser;

CREATE SEQUENCE users_test_scheme.users_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;
ALTER SEQUENCE users_test_scheme.users_id_seq
    OWNER TO microuser;


CREATE TABLE IF NOT EXISTS users_test_scheme.genders
(
    id smallint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    CONSTRAINT genders_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;
ALTER TABLE users_test_scheme.genders
    OWNER to microuser;


CREATE TABLE IF NOT EXISTS users_test_scheme.cities
(
    id integer NOT NULL DEFAULT nextval('users_test_scheme.cities_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT cities_pkey PRIMARY KEY (id),
    CONSTRAINT cities_name_uniq UNIQUE (name)

)
TABLESPACE pg_default;
ALTER TABLE users_test_scheme.cities
    OWNER to microuser;


CREATE TABLE IF NOT EXISTS users_test_scheme.users
(
    id integer NOT NULL DEFAULT nextval('users_test_scheme.users_id_seq'::regclass),
    first_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    second_name character varying(255) COLLATE pg_catalog."default",
    gender_id smallint NOT NULL,
    birthday timestamp without time zone NOT NULL,
    current_city_id integer,
    nickname character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    phone character varying(255) COLLATE pg_catalog."default",
    about text COLLATE pg_catalog."default",
    hard_skills text COLLATE pg_catalog."default",
    is_deleted boolean NOT NULL DEFAULT false,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_nickname_uniq UNIQUE (nickname),
    CONSTRAINT users_current_city_id_fkey FOREIGN KEY (current_city_id)
        REFERENCES users_test_scheme.cities (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT,
    CONSTRAINT users_gender_id_fkey FOREIGN KEY (gender_id)
        REFERENCES users_test_scheme.genders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;
ALTER TABLE users_test_scheme.users
    OWNER to microuser;
COMMENT ON COLUMN users_test_scheme.users.is_deleted
    IS 'For Soft Delete pattern';
CREATE INDEX fki_users_current_city_id_fkey
    ON users_test_scheme.users USING btree
    (current_city_id)
    TABLESPACE pg_default;
CREATE INDEX fki_users_gender_id_fkey
    ON users_test_scheme.users USING btree
    (gender_id)
    TABLESPACE pg_default;
CREATE INDEX i_users_gender_id_and_current_city_id
    ON users_test_scheme.users USING btree
    (gender_id, current_city_id)
    TABLESPACE pg_default;


CREATE TABLE IF NOT EXISTS users_test_scheme.follows
(
    id bigint NOT NULL DEFAULT nextval('users_test_scheme.follows_id_seq'::regclass),
    following_id bigint NOT NULL,
    follower_id bigint NOT NULL,
    followed_at timestamp without time zone NOT NULL,
    refers_deleted_user boolean NOT NULL DEFAULT false,
    CONSTRAINT follows_pkey PRIMARY KEY (id),
    CONSTRAINT follows_follower_id_fkey FOREIGN KEY (follower_id)
        REFERENCES users_test_scheme.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT follows_following_id_fkey FOREIGN KEY (following_id)
        REFERENCES users_test_scheme.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;
ALTER TABLE users_test_scheme.follows
    OWNER to microuser;
CREATE INDEX i_follows_following_id_and_follower_id
    ON users_test_scheme.follows USING btree
    (following_id, follower_id)
    TABLESPACE pg_default;