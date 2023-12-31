package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.util.Collection;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackSchedulerUnitTest {

    @Mock
    AudioPlayer player;

    @Mock
    AudioTrack track;

    TrackScheduler scheduler;

    @BeforeEach
    void setup() {
        scheduler = new TrackScheduler();
    }

    // TODO: 15.08.2023 Expand test cases after implementing new methods in TrackScheduler
    
    @Test
    void shouldQueue() {
        when(player.getPlayingTrack())
                .thenReturn(null, track, track);
        IntStream.range(0, 3)
                .forEach(i -> scheduler.queue(player, track));

        verify(player, times(3)).getPlayingTrack();
        verify(player, times(1)).playTrack(any());
    }

    @Test
    void shouldSkip() {
        when(player.getPlayingTrack())
                .thenReturn(null, track, track, track);

        IntStream.range(0, 3)
                .forEach(i -> scheduler.queue(player, track));

        scheduler.skip(player);

        verify(player, times(4)).getPlayingTrack();
        verify(player, times(2)).playTrack(any());
    }

    @Test
    void shouldOnTrackEnd() {
        scheduler.onTrackEnd(player, track, AudioTrackEndReason.FINISHED);
        scheduler.onTrackEnd(player, track, AudioTrackEndReason.STOPPED);
        verify(player, times(1)).playTrack(any());
    }

    @Test
    void shouldStop() {
        scheduler.stop(player);
        verify(player, times(1)).setPaused(true);
    }

    @Test
    void shouldResume() {
        scheduler.resume(player);
        verify(player, times(1)).setPaused(false);
    }

    @Test
    void shouldClean() {
        scheduler.clean(player);
        scheduler.skip(player);
        verify(player, times(1)).stopTrack();
        verify(player, never()).playTrack(any());
    }

    @Test
    void shouldGetQueue() {
        when(player.getPlayingTrack())
                .thenReturn(null, track, track);
        IntStream.range(0, 3)
                .forEach(i -> scheduler.queue(player, track));

        Collection<AudioTrack> queue = scheduler.getQueue();
        assertEquals(2, queue.size());
    }
}