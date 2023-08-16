package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContext;
import com.laa66.goodboi.music.AudioContextRepository;
import com.laa66.goodboi.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkipCommandUnitTest {

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
        doNothing().when(scheduler).skip(player);

        SkipCommand command = new SkipCommand(event, repository);

        StepVerifier.create(command.execute())
                .expectSubscription()
                .verifyComplete();

        verify(event, times(1)).getGuildId();
        verify(repository, times(1)).getContext(1);
        verify(context, times(1)).getScheduler();
        verify(context, times(1)).getPlayer();
        verify(scheduler, times(1)).skip(player);
    }

    @Test
    void shouldExecuteInvalidGuildId() {
        when(event.getGuildId()).thenReturn(Optional.empty());

        SkipCommand command = new SkipCommand(event, repository);
        assertThrows(NoSuchElementException.class, command::execute);


        verify(event, times(1)).getGuildId();
    }
}
