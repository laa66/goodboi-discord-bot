package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContext;
import com.laa66.goodboi.music.AudioContextRepository;
import com.laa66.goodboi.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrintQueueCommandUnitTest {

    @Mock
    MessageCreateEvent event;

    @Mock
    AudioContextRepository repository;

    @Mock
    Message message;

    @Mock
    MessageChannel channel;

    @Mock
    Message queueMessage;

    @Mock
    AudioContext context;

    @Mock
    TrackScheduler scheduler;

    @Mock
    AudioTrack track;

    @Mock
    AudioTrackInfo trackInfo;

    @Test
    void shouldExecuteValidGuildId() {
        when(event.getMessage()).thenReturn(message);
        when(message.getChannel()).thenReturn(Mono.just(channel));
        when(channel.createMessage(MessageCreateSpec
                .builder()
                .content("""
                        \uD83C\uDFB5 Goodboi music queue
                        null
                        null
                        """)
                .build())).thenReturn(Mono.just(queueMessage));
        when(event.getGuildId()).thenReturn(Optional.of(Snowflake.of(1)));
        when(repository.getContext(1)).thenReturn(context);
        when(context.getScheduler()).thenReturn(scheduler);
        when(scheduler.getQueue()).thenReturn(List.of(track, track));
        when(track.getInfo()).thenReturn(trackInfo);
        PrintQueueCommand command = new PrintQueueCommand(event, repository);
        StepVerifier.create(command.execute())
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void shouldExecuteInvalidGuildId() {
        when(event.getMessage()).thenReturn(message);
        when(message.getChannel()).thenReturn(Mono.just(channel));
        when(event.getGuildId()).thenReturn(Optional.empty());
        PrintQueueCommand command = new PrintQueueCommand(event, repository);
        StepVerifier.create(command.execute())
                .expectError(NoSuchElementException.class)
                .verify();
    }

}