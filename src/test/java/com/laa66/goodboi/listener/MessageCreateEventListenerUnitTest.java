package com.laa66.goodboi.listener;

import com.laa66.goodboi.command.Command;
import com.laa66.goodboi.command.CommandFactory;
import com.laa66.goodboi.command.CommandType;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageCreateEventListenerUnitTest {

    @Mock
    MessageCreateEvent event;

    @Mock
    CommandFactory commandFactory;

    @Mock
    Message message;

    @Mock
    Command command;

    @Mock
    User user;

    @InjectMocks
    MessageCreateEventListener messageCreateEventListener;

    @Test
    void shouldGetEventType() {
        assertEquals(MessageCreateEvent.class, messageCreateEventListener.getEventType());
    }

    @Test
    void shouldProcessValidCommand() {
        when(message.getAuthor()).thenReturn(Optional.of(user));
        when(user.isBot()).thenReturn(false);
        when(message.isPinned()).thenReturn(false);
        when(message.getContent()).thenReturn("!push");
        when(event.getMessage()).thenReturn(message);
        when(commandFactory.create(CommandType.PUSH_MESSAGE, message)).thenReturn(command);
        when(command.execute()).thenReturn(Mono.empty());

        Mono<Void> result = messageCreateEventListener.process(event);
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();

        verify(event, times(1)).getMessage();
        verify(message, times(1)).getAuthor();
        verify(user, times(1)).isBot();
        verify(message, times(1)).isPinned();
        verify(message, times(2)).getContent();
        verify(commandFactory, times(1)).create(any(), any());
        verify(command, times(1)).execute();
    }

    @Test
    void shouldProcessInvalidCommandUserIsBot() {
        when(event.getMessage()).thenReturn(message);
        when(message.getAuthor()).thenReturn(Optional.of(user));
        when(user.isBot()).thenReturn(true);
        Mono<Void> process = messageCreateEventListener.process(event);
        StepVerifier.create(process)
                .expectSubscription()
                .verifyComplete();

        verify(user, times(1)).isBot();
        verifyNoInteractions(command, commandFactory);
    }

    @Test
    void shouldProcessInvalidCommandIsPinned() {
        when(event.getMessage()).thenReturn(message);
        when(message.getAuthor()).thenReturn(Optional.of(user));
        when(user.isBot()).thenReturn(false);
        when(message.isPinned()).thenReturn(true);
        Mono<Void> process = messageCreateEventListener.process(event);
        StepVerifier.create(process)
                .expectSubscription()
                .verifyComplete();

        verify(message, times(1)).isPinned();
        verifyNoInteractions(command, commandFactory);
    }

    @Test
    void shouldProcessInvalidCommandNotStartsWith() {
        when(event.getMessage()).thenReturn(message);
        when(message.getAuthor()).thenReturn(Optional.of(user));
        when(user.isBot()).thenReturn(false);
        when(message.isPinned()).thenReturn(false);
        when(message.getContent()).thenReturn("invalid");
        Mono<Void> process = messageCreateEventListener.process(event);
        StepVerifier.create(process)
                .expectSubscription()
                .verifyComplete();

        verify(message, times(1)).getContent();
        verifyNoInteractions(command, commandFactory);
    }

    @Test
    void shouldProcessInvalidCommand() {
        when(event.getMessage()).thenReturn(message);
        when(message.getAuthor()).thenReturn(Optional.of(user));
        when(user.isBot()).thenReturn(false);
        when(message.isPinned()).thenReturn(false);
        when(message.getContent()).thenReturn("!invalid");
        Mono<Void> process = messageCreateEventListener.process(event);
        StepVerifier.create(process)
                .expectSubscription()
                .verifyComplete();

        verify(message, times(2)).getContent();
        verifyNoInteractions(command, commandFactory);
    }

}