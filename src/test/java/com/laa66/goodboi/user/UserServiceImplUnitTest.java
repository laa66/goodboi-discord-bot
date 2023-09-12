package com.laa66.goodboi.user;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.discordjson.json.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    Member member;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setup() {
        when(member.getGuildId()).thenReturn(Snowflake.of(77L));
        when(member.getUserData()).thenReturn(UserData.builder()
                .id(1L)
                .username("username")
                .discriminator("disc")
                .build());
    }

    @Test
    void shouldWarnUserNotExists() {
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.empty());
        when(userRepository.save(any())).thenReturn(Mono.just(User.builder().build()));

        Mono<Void> mono = userService.warn(member);
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
        User user = createTestUser(6);
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.just(user));
        when(userRepository.save(any())).thenReturn(Mono.just(user));

        Mono<Void> mono = userService.warn(member);
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
        User user = createTestUser(9);
        when(userRepository.findByGuildIdAndDiscordId(anyLong(), anyLong()))
                .thenReturn(Mono.just(user));
        when(userRepository.save(any())).thenReturn(Mono.just(user));

        Mono<Void> mono = userService.warn(member);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

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

    private static User createTestUser(int warnCount) {
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