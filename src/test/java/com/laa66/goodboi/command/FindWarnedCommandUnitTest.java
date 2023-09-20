package com.laa66.goodboi.command;

import com.laa66.goodboi.user.User;
import com.laa66.goodboi.user.UserService;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.InteractionCallbackSpecDeferReplyMono;
import discord4j.core.spec.InteractionFollowupCreateSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindWarnedCommandUnitTest {

    @Mock
    InteractionCallbackSpecDeferReplyMono callbackSpecDeferReplyMono;

    @Mock
    ChatInputInteractionEvent event;

    @Mock
    UserService userService;

    @Mock
    Message message;

    @Mock
    Interaction interaction;


    @Test
    void shouldExecute() {
        User user1 = User.builder()
                .id(1)
                .guildId(77L)
                .discordId(1L)
                .username("username1")
                .warnCount(4)
                .banned(false)
                .build();
        User user2 = User.builder()
                .id(2)
                .guildId(77L)
                .discordId(2L)
                .username("username2")
                .warnCount(2)
                .banned(false)
                .build();
        when(event.createFollowup(any(InteractionFollowupCreateSpec.class)))
                .thenReturn(Mono.just(message));
        when(event.deferReply()).thenReturn(callbackSpecDeferReplyMono);
        when(userService.findAllUsers(anyLong())).thenReturn(Flux.just(user1, user2));
        when(event.getInteraction()).thenReturn(interaction);
        when(interaction.getGuildId()).thenReturn(Optional.of(Snowflake.of(77L)));
        when(callbackSpecDeferReplyMono.then(any())).thenReturn(Mono.empty());

        FindWarnedCommand command = new FindWarnedCommand(event, userService);
        Mono<Void> mono = command.execute();
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(event, times(1)).deferReply();
        verify(event, times(1)).createFollowup(any(InteractionFollowupCreateSpec.class));
        verify(userService, times(1)).findAllUsers(77L);
        verify(event, times(1)).getInteraction();
    }

}