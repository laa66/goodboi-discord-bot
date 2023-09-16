package com.laa66.goodboi.filter;

import com.laa66.goodboi.user.UserService;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OffensiveMessageValidationServiceUnitTest {

    @Mock
    Message message;

    @Mock
    Member member;

    @Mock
    UserService userService;

    @InjectMocks
    OffensiveMessageValidationService validationService;

    @Test
    void shouldFilterMessageOffensive() {
        when(message.getContent()).thenReturn("random if some l2,11m2 ass");
        when(message.getAuthorAsMember()).thenReturn(Mono.just(member));
        when(userService.warn(member)).thenReturn(Mono.empty());

        Mono<Void> mono = validationService.filterMessage(message);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(userService, times(1)).warn(member);
    }

    @Test
    void shouldFilterMessageNotOffensive() {
        when(message.getContent()).thenReturn("random if some l2,11m2");

        Mono<Void> mono = validationService.filterMessage(message);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(userService, never()).warn(any());
    }

}