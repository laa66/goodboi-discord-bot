package com.laa66.goodboi.user;

import com.laa66.goodboi.repository.PostgreSQLBaseContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryIntegrationTest extends PostgreSQLBaseContainer {

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldFindByGuildIdAndDiscordIdExist() {
        Mono<User> mono = userRepository.findByGuildIdAndDiscordId(1, 2);
        StepVerifier.create(mono)
                .expectSubscription()
                .assertNext(user -> {
                    assertEquals("username2", user.getUsername());
                    assertEquals(2, user.getDiscordId());
                    assertFalse(user.isBanned());
                })
                .verifyComplete();
    }


    @Test
    void shouldFindByGuildIdAndDiscordIdNotExist() {
        Mono<User> mono = userRepository.findByGuildIdAndDiscordId(1, 4);
        StepVerifier.create(mono)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldFindAllByGuildIdExist() {
        Flux<User> flux = userRepository.findAllByGuildId(2);
        StepVerifier.create(flux)
                .assertNext(user -> assertEquals("username4", user.getUsername()))
                .assertNext(user -> assertEquals("username5", user.getUsername()))
                .verifyComplete();
    }

    @Test
    void shouldFindAllByGuildIdNotExist() {
        Flux<User> flux = userRepository.findAllByGuildId(3);
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldFindAllBannedExist() {
        Flux<User> flux = userRepository.findAllBanned(1);
        StepVerifier.create(flux)
                .expectSubscription()
                .assertNext(user -> assertEquals(1, user.getDiscordId()))
                .assertNext(user -> assertEquals(3, user.getDiscordId()))
                .verifyComplete();
    }

    @Test
    void shouldFindAllBannedNotExist() {
        Flux<User> flux = userRepository.findAllBanned(3);
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }
}