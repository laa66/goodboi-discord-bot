package com.laa66.goodboi.user;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends R2dbcRepository<User, Long> {
    Flux<User> findAllByGuildId(long guildId);

    @Query("SELECT * FROM users WHERE guild_id = :guildId AND banned = true")
    Flux<User> findAllBanned(long guildId);
}
