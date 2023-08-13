package com.laa66.goodboi.music;

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
class AudioContextCacheRepositoryUnitTest {

    @Mock
    Cache cache;

    @Mock
    AudioContext context;

    @InjectMocks
    AudioContextCacheRepository audioContextCacheRepository;

    @Test
    void shouldGetPlayerValidGuildId() {
        when(cache.get(1L, AudioContext.class)).thenReturn(context);
        AudioContext result = audioContextCacheRepository.getContext(1L);
        assertEquals(context, result);

        when(cache.get(2L, AudioContext.class)).thenReturn(null);
        AudioContext resultEmpty = audioContextCacheRepository.getContext(2L);
        assertNull(resultEmpty);
    }

    @Test
    void shouldSavePlayer() {
        audioContextCacheRepository.saveContext(1L, context);
        verify(cache, times(1)).putIfAbsent(1L, context);
    }

    @Test
    void shouldRemovePlayer() {
        audioContextCacheRepository.removeContext(1L);
        verify(cache, times(1)).evictIfPresent(1L);
    }

}