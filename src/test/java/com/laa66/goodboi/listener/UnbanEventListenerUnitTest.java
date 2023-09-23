package com.laa66.goodboi.listener;

import com.laa66.goodboi.user.UserService;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.guild.UnbanEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
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
class UnbanEventListenerUnitTest {

    @Mock
    UnbanEvent event;

    @Mock
    User user;

    @Mock
    Member member;

    @Mock
    UserService userService;

    @InjectMocks
    UnbanEventListener eventListener;

    @Test
    void shouldGetEventType() {
        assertEquals(UnbanEvent.class, eventListener.getEventType());
    }

    @Test
    void shouldProcess() {
        when(event.getUser()).thenReturn(user);
        when(user.asMember(any())).thenReturn(Mono.just(member));
        when(event.getGuildId()).thenReturn(Snowflake.of(77L));
        when(userService.unban(member)).thenReturn(Mono.empty());

        Mono<Void> mono = eventListener.process(event);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(event, times(1)).getUser();
        verify(user, times(1)).asMember(Snowflake.of(77L));
        verify(event, times(1)).getGuildId();
        verify(userService, times(1)).unban(member);
    }

}