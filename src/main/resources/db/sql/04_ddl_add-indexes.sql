CREATE INDEX fki_users_gender_id_fkey
    ON users_scheme.users(gender_id);
CREATE INDEX fki_users_current_city_id_fkey
    ON users_scheme.users(current_city_id);
CREATE INDEX i_users_gender_id_and_current_city_id
    ON users_scheme.users(gender_id, current_city_id);


CREATE INDEX i_follows_following_id_and_follower_id
    ON users_scheme.follows(following_id, follower_id);
