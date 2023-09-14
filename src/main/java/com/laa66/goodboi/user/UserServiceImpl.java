package com.laa66.goodboi.user;

import discord4j.core.object.entity.Member;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<Void> warn(Member member) {
        return userRepository.findByGuildIdAndDiscordId(member.getGuildId().asLong(),
                        member.getUserData().id().asLong())
                .flatMap(user -> Mono.just(user.withWarnCount(user.getWarnCount() + 1)))
                .flatMap(user -> Mono.just(user.getWarnCount() == 10 ? user.withBanned(true) : user)).defaultIfEmpty(User.builder()
                        .id(0)
                        .guildId(member.getGuildId().asLong())
                        .discordId(member.getUserData().id().asLong())
                        .username(member.getUserData().username())
                        .warnCount(1)
                        .banned(false)
                        .build())
                .flatMap(userRepository::save)
                .then();
    }

    @Override
    public Mono<Void> ban(Member member) {
        return userRepository.findByGuildIdAndDiscordId(member.getGuildId().asLong(),
                        member.getUserData().id().asLong())
                .flatMap(user -> userRepository.save(user.withBanned(true)))
                .then();
    }

    @Override
    public Mono<Void> unban(Member member) {
        return userRepository.findByGuildIdAndDiscordId(member.getGuildId().asLong(),
                member.getUserData().id().asLong())
                .flatMap(user -> userRepository.save(user
                        .withBanned(false)
                        .withWarnCount(user.getWarnCount() == 10 ? 0 : user.getWarnCount())))
                .then();
    }

    @Override
    public Flux<User> findAllUsers(long guildId) {
        return userRepository.findAllByGuildId(guildId);
    }

    @Override
    public Flux<User> findRudestUsers(long guildId) {
        return userRepository.findAllByGuildId(guildId)
                .sort(Comparator.comparingInt(User::getWarnCount))
                .take(10);
    }

    @Override
    public Flux<User> findBannedUsers(long guildId) {
        return userRepository.findAllBanned(guildId);
    }
}
