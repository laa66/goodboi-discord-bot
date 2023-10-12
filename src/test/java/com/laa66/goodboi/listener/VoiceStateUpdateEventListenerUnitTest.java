package com.laa66.goodboi.listener;

import com.laa66.goodboi.voiceactivity.VoiceChannelActivityTrackingService;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoiceStateUpdateEventListenerUnitTest {

    @Mock
    VoiceStateUpdateEvent event;

    @Mock
    VoiceChannelActivityTrackingService voiceChannelActivityTrackingService;

    @InjectMocks
    VoiceStateUpdateEventListener voiceStateUpdateEventListener;

    @Test
    void shouldGetEventType() {
        assertEquals(VoiceStateUpdateEvent.class, voiceStateUpdateEventListener.getEventType());
    }

    @Test
    void shouldProcess() {
        when(voiceChannelActivityTrackingService.onVoiceChannelEvent(any()))
                .thenReturn(Mono.empty());

        Mono<Void> mono = voiceStateUpdateEventListener.process(event);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();
        verify(voiceChannelActivityTrackingService, times(1)).onVoiceChannelEvent(event);
    }

}
