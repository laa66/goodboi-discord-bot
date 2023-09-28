package com.laa66.goodboi.voiceactivity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
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
    void shouldGetVoiceActivityExist() {
        when(cache.get("77#212", VoiceActivity.class)).thenReturn(voiceActivity);

        Mono<VoiceActivity> mono = repository.getVoiceActivity(77, 212);
        StepVerifier.create(mono)
                .expectSubscription()
                .expectNext(voiceActivity)
                .verifyComplete();

        verify(cache, times(1)).get("77#212", VoiceActivity.class);
    }

    @Test
    void shouldGetVoiceActivityNotExist() {
        when(cache.get("77#212", VoiceActivity.class)).thenReturn(null);

        Mono<VoiceActivity> mono = repository.getVoiceActivity(77, 212);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(cache, times(1)).get("77#212", VoiceActivity.class);

    }

}