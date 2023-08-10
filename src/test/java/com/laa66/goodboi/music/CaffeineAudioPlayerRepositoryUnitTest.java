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

    @InjectMocks
    CaffeineAudioPlayerRepository caffeineAudioPlayerRepository;

    @Test
    void shouldGetPlayerValidGuildId() {
        AudioPlayer player = mock(AudioPlayer.class);
        when(cache.get("id", AudioPlayer.class)).thenReturn(player);
        AudioPlayer result = caffeineAudioPlayerRepository.getPlayer("id");
        assertEquals(player, result);

    }

    @Test
    void shouldGetPlayerInvalidGuildId() {
        when(cache.get("invalidId", AudioPlayer.class)).thenReturn(null);
        assertThrows(CacheEntryNotFoundException.class, () -> caffeineAudioPlayerRepository.getPlayer("invalidId"));
    }

    @Test
    void shouldSavePlayer() {
        AudioPlayer player = mock(AudioPlayer.class);
        caffeineAudioPlayerRepository.savePlayer("id", player);
        verify(cache, times(1)).putIfAbsent("id", player);
    }

    @Test
    void shouldRemovePlayer() {
        caffeineAudioPlayerRepository.removePlayer("id");
        verify(cache, times(1)).evictIfPresent("id");
    }

}