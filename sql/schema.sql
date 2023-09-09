CREATE TABLE users (
	id INT AUTO_INCREMENT,
    discord_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
    warn_count INT NOT NULL,
    banned BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (discord_id)
);