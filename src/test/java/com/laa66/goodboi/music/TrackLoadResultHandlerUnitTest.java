package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrackLoadResultHandlerUnitTest {

    @Mock
    AudioTrack track;

    @Mock
    AudioPlayer player;

    @Mock
    TrackScheduler scheduler;

    TrackLoadResultHandler loadResultHandler;

    @BeforeEach
    void setup() {
        loadResultHandler = new TrackLoadResultHandler(scheduler, player);
    }

    @Test
    void shouldTrackLoaded() {
        loadResultHandler.trackLoaded(track);
        verify(scheduler, times(1)).queue(player, track);
    }
}