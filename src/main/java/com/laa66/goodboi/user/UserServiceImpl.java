package com.laa66.goodboi.user;

import discord4j.core.object.entity.Member;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<Void> warn(Member member) {
        return userRepository.findByGuildIdAndDiscordId(
                member.getGuildId().asLong(), member.getUserData().id().asLong()
                ).flatMap(user -> {
                    user.setWarnCount(user.getWarnCount() + 1);
                    if (user.getWarnCount() == 10) user.setBanned(true);
                    return Mono.just(user);
                }).defaultIfEmpty(User.builder()
                        .id(0)
                        .guildId(member.getGuildId().asLong())
                        .discordId(member.getUserData().id().asLong())
                        .username(member.getUserData().username())
                        .warnCount(1)
                        .banned(false)
                        .build())
                .doOnSuccess(userRepository::save)
                .then();
    }

    @Override
    public Mono<Void> ban(Member member) {
        return null;
    }

    @Override
    public Mono<Void> unban(Member member) {
        return null;
    }

    @Override
    public Flux<User> findAllUsers(long guildId) {
        return null;
    }

    @Override
    public Flux<User> findRudestUsers(long guildId) {
        return null;
    }

    @Override
    public Flux<User> findBannedUsers(long guildId) {
        return null;
    }
}
