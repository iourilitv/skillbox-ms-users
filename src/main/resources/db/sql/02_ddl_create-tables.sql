CREATE TABLE IF NOT EXISTS users_scheme.genders (
	id smallint NOT NULL PRIMARY KEY,
	name varchar(255) NOT NULL,
	description text
);
ALTER TABLE users_scheme.genders
	OWNER to microuser;


CREATE TABLE IF NOT EXISTS users_scheme.cities (
	id serial PRIMARY KEY,
	name varchar(255) NOT NULL
);
ALTER TABLE users_scheme.cities
    ADD CONSTRAINT cities_name_uniq UNIQUE (name);
ALTER TABLE users_scheme.cities
	OWNER to microuser;


CREATE TABLE IF NOT EXISTS users_scheme.users (
	id bigserial PRIMARY KEY,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	second_name varchar(255),
	gender_id smallint NOT NULL,
	birthday timestamp NOT NULL,
	current_city_id integer NOT NULL,
	nickname varchar(255) NOT NULL,
	email varchar(255),
	phone varchar(255),
	about text,
	hard_skills text
);
ALTER TABLE users_scheme.users
    ADD CONSTRAINT users_current_city_id_fkey FOREIGN KEY (current_city_id)
    REFERENCES users_scheme.cities (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE RESTRICT;
ALTER TABLE users_scheme.users
    ADD CONSTRAINT users_gender_id_fkey FOREIGN KEY (gender_id)
    REFERENCES users_scheme.genders (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE RESTRICT;
ALTER TABLE users_scheme.users
    ADD CONSTRAINT users_nickname_uniq UNIQUE (nickname);
ALTER TABLE users_scheme.users
	OWNER to microuser;


CREATE TABLE IF NOT EXISTS users_scheme.follows (
	id bigserial PRIMARY KEY,
	following_id bigint NOT NULL,
	follower_id bigint NOT NULL,
	followed_at timestamp NOT NULL
);
ALTER TABLE users_scheme.follows
    ADD CONSTRAINT follows_following_id_fkey FOREIGN KEY (following_id)
    REFERENCES users_scheme.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    VALID;
ALTER TABLE users_scheme.follows
    ADD CONSTRAINT follows_follower_id_fkey FOREIGN KEY (follower_id)
    REFERENCES users_scheme.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    VALID;
ALTER TABLE users_scheme.follows
	OWNER to microuser;
