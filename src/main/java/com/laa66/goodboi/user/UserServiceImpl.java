package com.laa66.goodboi.user;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.BanQuerySpec;
import discord4j.core.spec.MessageCreateSpec;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<Void> warn(Member member, Message message) {
        return userRepository.findByGuildIdAndDiscordId(member.getGuildId().asLong(),
                        member.getUserData().id().asLong())
                .flatMap(user -> {
                    User updatedUser = user.withWarnCount(user.getWarnCount() + 1);
                    if (updatedUser.getWarnCount() >= 10) {
                        return member.ban(BanQuerySpec.builder()
                                        .reason("Profanity")
                                        .build())
                                .thenReturn(updatedUser.withBanned(true));
                    }
                    return Mono.just(updatedUser);
                })
                .defaultIfEmpty(User.builder()
                        .id(0)
                        .guildId(member.getGuildId().asLong())
                        .discordId(member.getUserData().id().asLong())
                        .username(member.getUserData().username())
                        .warnCount(1)
                        .banned(false)
                        .build())
                .flatMap(userRepository::save)
                .flatMap(user -> message
                        .getChannel()
                        .flatMap(channel -> sendWarnMessage(channel, user)));
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
                .sort(Comparator.comparingInt(User::getWarnCount).reversed())
                .take(10);
    }

    @Override
    public Flux<User> findBannedUsers(long guildId) {
        return userRepository.findAllBanned(guildId);
    }

    private Mono<Void> sendWarnMessage(MessageChannel channel, User user) {
        return channel.createMessage(MessageCreateSpec.builder()
                .content("<@" + user.getDiscordId() + "> have been warned " + user.getWarnCount() + " times!")
                .build())
                .then();
    }
}
