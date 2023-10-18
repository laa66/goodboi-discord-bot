CREATE SCHEMA goodboi_db;

CREATE TABLE goodboi_db.users (
	id SERIAL,
	guild_id BIGINT NOT NULL,
    discord_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
    warn_count INT NOT NULL,
    banned BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (discord_id)
);

INSERT INTO goodboi_db.users (guild_id, discord_id, username, warn_count, banned)
VALUES
    (1, 1, 'username1', 0, true),
    (1, 2, 'username2', 0, false),
    (1, 3, 'username3', 10, true),
    (2, 4, 'username4', 0, false),
    (2, 5, 'username5', 0, false),
    (1, 6, 'username6', 9, false);