package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioPlayerRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.VoiceChannelJoinSpec;
import discord4j.voice.VoiceConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JoinCommandUnitTest {

    @Mock
    MessageCreateEvent messageCreateEvent;

    @Mock
    AudioPlayerManager playerManager;

    @Mock
    AudioPlayerRepository playerRepository;

    @InjectMocks
    JoinCommand joinCommand;

    @Mock
    AudioPlayer player;

    @Mock
    Member member;

    @Mock
    VoiceState voiceState;

    @Mock
    VoiceChannel voiceChannel;

    @Mock
    VoiceConnection voiceConnection;

    @Test
    void shouldExecuteValidGuildIdPlayerPresent() {
        when(messageCreateEvent.getGuildId()).thenReturn(Optional.of(Snowflake.of(1)));
        when(playerRepository.getPlayer(1)).thenReturn(player);
        when(messageCreateEvent.getMember()).thenReturn(Optional.of(member));
        when(member.getVoiceState()).thenReturn(Mono.just(voiceState));
        when(voiceState.getChannel()).thenReturn(Mono.just(voiceChannel));
        when(voiceChannel.join(any(VoiceChannelJoinSpec.class))).thenReturn(Mono.just(voiceConnection));

        StepVerifier.create(joinCommand.execute())
                .expectSubscription()
                .verifyComplete();

        verify(messageCreateEvent, times(1)).getGuildId();
        verify(playerRepository, times(1)).getPlayer(1);
        verify(messageCreateEvent, times(1)).getMember();
        verify(member, times(1)).getVoiceState();
        verify(voiceState, times(1)).getChannel();
        verify(voiceChannel, times(1)).join(any(VoiceChannelJoinSpec.class));
    }

    @Test
    void shouldExecuteInvalidGuildId() {
        when(messageCreateEvent.getGuildId()).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> joinCommand.execute());
    }

    @Test
    void shouldExecuteEmptyAudioPlayerRepository() {
        when(messageCreateEvent.getGuildId()).thenReturn(Optional.of(Snowflake.of(1)));
        when(playerRepository.getPlayer(1)).thenReturn(null);
        when(playerManager.createPlayer()).thenReturn(player);
        when(messageCreateEvent.getMember()).thenReturn(Optional.of(member));
        when(member.getVoiceState()).thenReturn(Mono.just(voiceState));
        when(voiceState.getChannel()).thenReturn(Mono.just(voiceChannel));
        when(voiceChannel.join(any(VoiceChannelJoinSpec.class))).thenReturn(Mono.just(voiceConnection));

        StepVerifier.create(joinCommand.execute())
                .expectSubscription()
                .verifyComplete();

        verify(messageCreateEvent, times(1)).getGuildId();
        verify(playerRepository, times(1)).getPlayer(1);
        verify(playerManager, times(1)).createPlayer();
        verify(messageCreateEvent, times(1)).getMember();
        verify(member, times(1)).getVoiceState();
        verify(voiceState, times(1)).getChannel();
        verify(voiceChannel, times(1)).join(any(VoiceChannelJoinSpec.class));

    }
}