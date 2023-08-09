package com.laa66.goodboi.command;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PushCommandUnitTest {

    @Mock
    Message message;

    @Mock
    MessageChannel channel;

    @InjectMocks
    PushCommand command;

    @Test
    void shouldExecutePush() {
        when(message.getChannel()).thenReturn(Mono.just(channel));
        when(message.getContent()).thenReturn("!push");
        when(channel.createMessage(any(MessageCreateSpec.class))).thenReturn(Mono.just(message));
        Mono<Void> execute = command.execute();
        StepVerifier.create(execute)
                .expectSubscription()
                .verifyComplete();
        verify(message, times(1)).getChannel();
        verify(channel, times(1)).createMessage(any(MessageCreateSpec.class));
    }

}