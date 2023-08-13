package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContext;
import com.laa66.goodboi.music.AudioContextRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayCommandUnitTest {

    @Mock
    MessageCreateEvent event;

    @Mock
    AudioPlayerManager manager;

    @Mock
    AudioContextRepository contextRepository;

    @Mock
    Message message;

    @Mock
    AudioContext audioContext;

    @InjectMocks
    PlayCommand command;

    @Test
    void shouldExecuteValidGuild() {
        when(message.getContent()).thenReturn("!play link");
        when(event.getMessage()).thenReturn(message);
        when(event.getGuildId()).thenReturn(Optional.of(Snowflake.of(1)));
        when(contextRepository.getContext(1)).thenReturn(audioContext);

        StepVerifier.create(command.execute())
                .expectSubscription()
                .verifyComplete();

        verify(message, times(1)).getContent();
        verify(event, times(1)).getMessage();
        verify(event, times(1)).getGuildId();
        verify(contextRepository, times(1)).getContext(1);
        verify(manager, times(1)).loadItem("link", null);
    }

    @Test
    void shouldExecuteInvalidGuild() {
        when(message.getContent()).thenReturn("!play link");
        when(event.getMessage()).thenReturn(message);
        when(event.getGuildId()).thenReturn(Optional.empty());

        StepVerifier.create(command.execute())
                .expectSubscription()
                .expectError(NoSuchElementException.class)
                .verify();
    }

}