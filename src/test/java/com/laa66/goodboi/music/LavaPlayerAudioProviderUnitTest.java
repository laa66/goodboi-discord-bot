package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LavaPlayerAudioProviderUnitTest {

    @Mock
    AudioPlayer audioPlayer;

    @InjectMocks
    LavaPlayerAudioProvider playerAudioProvider;

    @Test
    void shouldProvideIsProvided() {
        when(audioPlayer.provide(any())).thenReturn(true);
        boolean result = playerAudioProvider.provide();
        assertTrue(result);
    }

    @Test
    void shouldProvideIsNotProvided() {
        when(audioPlayer.provide(any())).thenReturn(false);
        boolean result = playerAudioProvider.provide();
        assertFalse(result);
    }

}