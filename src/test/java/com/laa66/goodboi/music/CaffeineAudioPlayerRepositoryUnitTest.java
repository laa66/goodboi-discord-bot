package com.laa66.goodboi.music;

import com.laa66.goodboi.exception.CacheEntryNotFoundException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaffeineAudioPlayerRepositoryUnitTest {

    @Mock
    Cache cache;

    @Mock
    AudioPlayer player;

    @InjectMocks
    CaffeineAudioPlayerRepository caffeineAudioPlayerRepository;

    @Test
    void shouldGetPlayerValidGuildId() {
        when(cache.get(1L, AudioPlayer.class)).thenReturn(player);
        AudioPlayer result = caffeineAudioPlayerRepository.getPlayer(1L);
        assertEquals(player, result);

        when(cache.get(2L, AudioPlayer.class)).thenReturn(null);
        AudioPlayer resultEmpty = caffeineAudioPlayerRepository.getPlayer(2L);
        assertNull(resultEmpty);
    }

    @Test
    void shouldSavePlayer() {
        caffeineAudioPlayerRepository.savePlayer(1L, player);
        verify(cache, times(1)).putIfAbsent(1L, player);
    }

    @Test
    void shouldRemovePlayer() {
        caffeineAudioPlayerRepository.removePlayer(1L);
        verify(cache, times(1)).evictIfPresent(1L);
    }

}