package com.laa66.goodboi.voiceactivity;

import com.laa66.goodboi.exception.CacheNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

    @Test
    void shouldSaveVoiceActivityExist() {
        doNothing().when(cache).put("77#212", VoiceActivity.class);

        Mono<Void> mono = repository.saveVoiceActivity(77, 212, voiceActivity);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(cache, times(1)).put("77#212", VoiceActivity.class);
    }

    @Test
    void shouldSaveVoiceActivityNotExist() {
        VoiceChannelActivityRepository nullCacheRepository = new VoiceChannelActivityCacheRepository(null);

        Mono<Void> mono = nullCacheRepository.saveVoiceActivity(77, 212, voiceActivity);
        StepVerifier.create(mono)
                .expectSubscription()
                .expectError(CacheNotFoundException.class)
                .verify();
    }

}