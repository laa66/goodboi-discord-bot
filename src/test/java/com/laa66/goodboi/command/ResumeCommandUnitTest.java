package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContext;
import com.laa66.goodboi.music.AudioContextRepository;
import com.laa66.goodboi.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeCommandUnitTest {

    @Mock
    MessageCreateEvent event;

    @Mock
    AudioContextRepository repository;

    @Mock
    AudioContext context;

    @Mock
    TrackScheduler scheduler;

    @Mock
    AudioPlayer player;

    @Test
    void shouldExecuteValidGuildId() {
        when(event.getGuildId()).thenReturn(Optional.of(Snowflake.of(1)));
        when(repository.getContext(1)).thenReturn(context);
        when(context.getScheduler()).thenReturn(scheduler);
        when(context.getPlayer()).thenReturn(player);
        doNothing().when(scheduler).resume(player);

        ResumeCommand command = new ResumeCommand(event, repository);

        StepVerifier.create(command.execute())
                .expectSubscription()
                .verifyComplete();

        verify(event, times(1)).getGuildId();
        verify(repository, times(1)).getContext(1);
        verify(context, times(1)).getScheduler();
        verify(context, times(1)).getPlayer();
        verify(scheduler, times(1)).resume(player);
    }

    @Test
    void shouldExecuteInvalidGuildId() {
        when(event.getGuildId()).thenReturn(Optional.empty());

        ResumeCommand command = new ResumeCommand(event, repository);
        assertThrows(NoSuchElementException.class, command::execute);

        verify(event, times(1)).getGuildId();
    }
}