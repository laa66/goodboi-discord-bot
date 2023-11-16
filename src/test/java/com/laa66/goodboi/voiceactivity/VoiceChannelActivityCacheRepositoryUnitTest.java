package com.laa66.goodboi.voiceactivity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class VoiceChannelActivityCacheRepositoryUnitTest {

    @Mock
    Cache cache;

    @Mock
    VoiceActivity voiceActivity;

    @InjectMocks
    VoiceChannelActivityCacheRepository repository;

    @Test
    void shouldGetVoiceActivityEmptyGuildVoiceActivityMap() {
        when(cache.get(1L, Map.class)).thenReturn(null);
        VoiceActivity activity = repository.getVoiceActivity(1L, 25L);
        assertNull(activity);
        verify(cache, times(1)).put(eq(1L), any(Map.class));
    }

    @Test
    void shouldGetVoiceActivityPresentGuildVoiceActivityMap() {
        Map<Long, VoiceActivity> map = Map.of(25L, voiceActivity);
        when(cache.get(1L, Map.class)).thenReturn(map);
        VoiceActivity activity = repository.getVoiceActivity(1L, 25L);
        assertEquals(voiceActivity, activity);
    }

    @Test
    void shouldGetGuildVoiceActivity() {
        Map<Long, VoiceActivity> map = Map.of(25L, voiceActivity);
        when(cache.get(1L, Map.class)).thenReturn(map);
        Map<Long, VoiceActivity> activity = repository.getGuildVoiceActivity(1L);
        assertEquals(map, activity);
    }

    @Test
    void shouldSaveVoiceActivity() {
        Map<Long, VoiceActivity> map = new HashMap<>();
        map.put(25L, voiceActivity);
        when(cache.get(1L, Map.class)).thenReturn(map);
        Map<Long, VoiceActivity> activity = repository.saveVoiceActivity(1L, 27L, voiceActivity);
        assertEquals(2, activity.size());
    }

}