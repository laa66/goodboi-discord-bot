package com.laa66.goodboi.command;


import com.laa66.goodboi.music.AudioContextRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.VoiceConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExitCommandUnitTest {

    @Mock
    MessageCreateEvent event;

    @Mock
    AudioContextRepository audioContextRepository;

    @Mock
    Member member;

    @Mock
    VoiceState voiceState;

    @Mock
    VoiceChannel voiceChannel;

    @Mock
    VoiceConnection voiceConnection;

    @Test
    void shouldExecute() {
        when(event.getMember()).thenReturn(Optional.of(member));
        when(member.getVoiceState()).thenReturn(Mono.just(voiceState));
        when(voiceState.getChannel()).thenReturn(Mono.just(voiceChannel));
        when(voiceChannel.getVoiceConnection()).thenReturn(Mono.just(voiceConnection));
        when(voiceConnection.disconnect()).thenReturn(Mono.empty());
        when(event.getGuildId()).thenReturn(Optional.of(Snowflake.of(77L)));

        ExitCommand exitCommand = new ExitCommand(event, audioContextRepository);

        StepVerifier.create(exitCommand.execute())
                .expectSubscription()
                .verifyComplete();

        verify(audioContextRepository, times(1)).removeContext(77L);
    }

    @Test
    void shouldExecuteDisconnectError() {
        when(event.getMember()).thenReturn(Optional.of(member));
        when(member.getVoiceState()).thenReturn(Mono.just(voiceState));
        when(voiceState.getChannel()).thenReturn(Mono.just(voiceChannel));
        when(voiceChannel.getVoiceConnection()).thenReturn(Mono.just(voiceConnection));
        when(voiceConnection.disconnect()).thenReturn(Mono.error(RuntimeException::new));

        ExitCommand exitCommand = new ExitCommand(event, audioContextRepository);

        StepVerifier.create(exitCommand.execute())
                .expectSubscription()
                .verifyError();

        verify(audioContextRepository, never()).removeContext(anyLong());
    }

    @Test
    void shouldExecuteEmptyMember() {
        when(event.getMember()).thenReturn(Optional.empty());
        ExitCommand exitCommand = new ExitCommand(event, audioContextRepository);
        assertThrows(NoSuchElementException.class, exitCommand::execute);
    }
}
