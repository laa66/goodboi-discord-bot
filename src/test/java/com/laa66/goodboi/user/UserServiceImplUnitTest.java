package com.laa66.goodboi.user;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.BanQuerySpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    Message message;

    @Mock
    Member member;

    @Mock
    MessageChannel channel;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void shouldWarnUserNotExists() {
        createExpectations();
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.empty());
        when(userRepository.save(any())).thenReturn(Mono.just(User.builder().build()));
        when(message.getChannel()).thenReturn(Mono.just(channel));
        when(channel.createMessage(any(MessageCreateSpec.class))).thenReturn(Mono.empty());

        Mono<Void> mono = userService.warn(member, message);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(userRepository, times(1))
                .findByGuildIdAndDiscordId(77, 1L);
        verify(userRepository, times(1))
                .save(argThat(arg -> arg.getId() == 0
                && arg.getWarnCount() == 1
                && arg.getDiscordId() == 1
                && !arg.isBanned()));
    }

    @Test
    void shouldWarnUserExistsNoBan() {
        createExpectations();
        User user = createTestUser(6);
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.just(user));
        when(userRepository.save(any())).thenReturn(Mono.just(user));
        when(message.getChannel()).thenReturn(Mono.just(channel));
        when(channel.createMessage(any(MessageCreateSpec.class))).thenReturn(Mono.empty());

        Mono<Void> mono = userService.warn(member, message);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(userRepository, times(1))
                .findByGuildIdAndDiscordId(77, 1L);
        verify(userRepository, times(1))
                .save(argThat(arg -> arg.getId() == 1
                        && arg.getWarnCount() == 7
                        && arg.getDiscordId() == 1
                        && !arg.isBanned()));
    }

    @Test
    void shouldWarnUserExistsBan() {
        createExpectations();
        User user = createTestUser(9);
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.just(user));
        when(userRepository.save(any())).thenReturn(Mono.just(user));
        when(message.getChannel()).thenReturn(Mono.just(channel));
        when(channel.createMessage(any(MessageCreateSpec.class))).thenReturn(Mono.empty());
        when(member.ban(any(BanQuerySpec.class))).thenReturn(Mono.empty());

        Mono<Void> mono = userService.warn(member, message);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(member, times(1)).ban(BanQuerySpec.builder().reason("Profanity").build());
        verify(userRepository, times(1))
                .findByGuildIdAndDiscordId(77, 1L);
        verify(userRepository, times(1))
                .save(argThat(arg -> arg.getId() == 1
                        && arg.getWarnCount() == 10
                        && arg.getDiscordId() == 1
                        && arg.isBanned()));
    }


    @Test
    void shouldBanUserExists() {
        createExpectations();
        User user = createTestUser(6);
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.just(user));
        when(userRepository.save(any())).thenReturn(Mono.just(user));

        Mono<Void> mono = userService.ban(member);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(userRepository, times(1))
                .findByGuildIdAndDiscordId(77, 1L);
        verify(userRepository, times(1))
                .save(argThat(arg -> arg.getId() == 1
                        && arg.isBanned()));
    }

    @Test
    void shouldBanUserNotExists() {
        createExpectations();
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.empty());

        Mono<Void> mono = userService.ban(member);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(userRepository, times(1))
                .findByGuildIdAndDiscordId(77, 1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUnbanUserExistsVulgarismBan() {
        createExpectations();
        User user = createTestUser(10).withBanned(true);
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.just(user));
        when(userRepository.save(any())).thenReturn(Mono.just(user));

        Mono<Void> mono = userService.unban(member);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(userRepository, times(1))
                .findByGuildIdAndDiscordId(77, 1L);
        verify(userRepository, times(1))
                .save(argThat(arg -> !arg.isBanned() && arg.getWarnCount() == 0));
    }

    @Test
    void shouldUnbanUserExistsOtherBan() {
        createExpectations();
        User user = createTestUser(6).withBanned(true);
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.just(user));
        when(userRepository.save(any())).thenReturn(Mono.just(user));

        Mono<Void> mono = userService.unban(member);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(userRepository, times(1))
                .findByGuildIdAndDiscordId(77, 1L);
        verify(userRepository, times(1))
                .save(argThat(arg -> !arg.isBanned() && arg.getWarnCount() == 6));
    }

    @Test
    void shouldUnbanUserNotExists() {
        createExpectations();
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.empty());

        Mono<Void> mono = userService.unban(member);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(userRepository, times(1))
                .findByGuildIdAndDiscordId(77, 1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldFindAllUsers() {
        User user1 = createTestUser(5);
        User user2 = createTestUser(1);
        when(userRepository.findAllByGuildId(anyLong()))
                .thenReturn(Flux.just(user1, user2));

        Flux<User> flux = userService.findAllUsers(77);
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(user1, user2)
                .verifyComplete();

        verify(userRepository, times(1)).findAllByGuildId(77L);
    }

    @Test
    void shouldFindRudestUsers() {
        List<User> testUsers = List.of(createTestUser(6), createTestUser(7), createTestUser(1), createTestUser(1),
                createTestUser(1), createTestUser(1), createTestUser(1), createTestUser(1),
                createTestUser(1), createTestUser(1), createTestUser(10), createTestUser(1));
        when(userRepository.findAllByGuildId(anyLong()))
                .thenReturn(Flux.fromIterable(testUsers));

        Flux<User> flux = userService.findRudestUsers(77L);
        StepVerifier.create(flux)
                .expectSubscription()
                .assertNext(user -> assertEquals(10, user.getWarnCount()))
                .assertNext(user -> assertEquals(7, user.getWarnCount()))
                .assertNext(user -> assertEquals(6, user.getWarnCount()))
                .expectNextCount(7)
                .verifyComplete();

        verify(userRepository, times(1)).findAllByGuildId(77L);
    }

    @Test
    void shouldFindBannedUsers() {
        List<User> testUsers = List.of(
                createTestUser(10).withBanned(true),
                createTestUser(5).withBanned(true));
        when(userRepository.findAllBanned(anyLong()))
                .thenReturn(Flux.fromIterable(testUsers));

        Flux<User> flux = userService.findBannedUsers(77L);
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();

    }

    // helpers
    private void createExpectations() {
        when(member.getGuildId()).thenReturn(Snowflake.of(77L));
        when(member.getUserData()).thenReturn(UserData.builder()
                .id(1L)
                .username("username")
                .discriminator("disc")
                .build());
    }

    private User createTestUser(int warnCount) {
        return User.builder()
                .id(1)
                .guildId(77L)
                .discordId(1L)
                .username("username")
                .warnCount(warnCount)
                .banned(false)
                .build();
    }

}