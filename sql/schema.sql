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