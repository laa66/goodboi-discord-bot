package com.laa66.goodboi.listener;

import com.laa66.goodboi.command.Command;
import com.laa66.goodboi.command.CommandFactory;
import com.laa66.goodboi.command.CommandType;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
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
class ApplicationCommandEventListenerUnitTest {

    @Mock
    ChatInputInteractionEvent event;

    @Mock
    CommandFactory commandFactory;

    @Mock
    Command command;

    @InjectMocks
    ApplicationCommandEventListener eventListener;

    @Test
    void shouldGetEventType() {
        assertEquals(ChatInputInteractionEvent.class, eventListener.getEventType());
    }

    @Test
    void shouldProcessValidCommand() {
        when(event.getCommandName()).thenReturn("rude");
        when(commandFactory.create(any(), any())).thenReturn(command);
        when(command.execute()).thenReturn(Mono.empty());

        Mono<Void> mono = eventListener.process(event);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(event, times(1)).getCommandName();
        verify(commandFactory, times(1)).create(CommandType.RUDEST_USERS, event);
        verify(command, times(1)).execute();
    }

    @Test
    void shouldProcessInvalidCommand() {
        when(event.getCommandName()).thenReturn("wrong");

        Mono<Void> mono = eventListener.process(event);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(event, times(1)).getCommandName();
        verify(commandFactory, never()).create(any(), any());
        verify(command, never()).execute();
    }

}