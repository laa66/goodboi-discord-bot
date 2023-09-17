package com.laa66.goodboi.user;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<Void> warn(Member member, Message message);

    Mono<Void> ban(Member member);

    Mono<Void> unban(Member member);

    Flux<User> findAllUsers(long guildId);

    Flux<User> findRudestUsers(long guildId);

    Flux<User> findBannedUsers(long guildId);
}
