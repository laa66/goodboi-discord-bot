package com.laa66.goodboi.voiceactivity;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoiceChannelActivityTrackingServiceImplUnitTest {

    @Mock
    VoiceChannelActivityRepository activityRepository;

    @Mock
    VoiceStateUpdateEvent event;

    @Mock
    VoiceState voiceState;

    @Mock
    Map<Long, VoiceActivity> voiceActivityMap;

    @InjectMocks
    VoiceChannelActivityTrackingServiceImpl trackingService;

    @Test
    void shouldOnVoiceChannelEventVoiceActivityExistJoin() {
        long voiceMillisTest = System.currentTimeMillis() - 600;
        VoiceActivity voiceActivity = new VoiceActivity("username", voiceMillisTest, 0L);
        when(event.getCurrent()).thenReturn(voiceState);
        when(voiceState.getGuildId()).thenReturn(Snowflake.of("1"));
        when(voiceState.getUserId()).thenReturn(Snowflake.of("2"));
        when(activityRepository.getVoiceActivity(anyLong(), anyLong()))
                .thenReturn(voiceActivity);
        when(event.isJoinEvent()).thenReturn(true);
        when(activityRepository.saveVoiceActivity(anyLong(), anyLong(), any())).thenReturn(voiceActivityMap);

        Mono<Void> mono = trackingService.onVoiceChannelEvent(event);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(activityRepository, times(1)).getVoiceActivity(1L, 2L);
        verify(activityRepository, times(1)).saveVoiceActivity(eq(1L), eq(2L), argThat(arg -> arg.getActiveTime() == 0
        && arg.getJoinedAt() > voiceMillisTest));
    }

    @Test
    void shouldOnVoiceChannelEventVoiceActivityExistLeave() {
        long voiceMillisTest = System.currentTimeMillis() - 600;
        VoiceActivity voiceActivity = new VoiceActivity("username", voiceMillisTest, 0L);
        when(event.getCurrent()).thenReturn(voiceState);
        when(voiceState.getGuildId()).thenReturn(Snowflake.of("1"));
        when(voiceState.getUserId()).thenReturn(Snowflake.of("2"));
        when(activityRepository.getVoiceActivity(anyLong(), anyLong()))
                .thenReturn(voiceActivity);
        when(event.isLeaveEvent()).thenReturn(true);
        when(activityRepository.saveVoiceActivity(anyLong(), anyLong(), any())).thenReturn(voiceActivityMap);

        Mono<Void> mono = trackingService.onVoiceChannelEvent(event);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(activityRepository, times(1)).getVoiceActivity(1L, 2L);
        verify(activityRepository, times(1)).saveVoiceActivity(eq(1L), eq(2L), argThat(arg -> arg.getActiveTime() >= 600));
    }

    @Test
    void shouldOnVoiceChannelEventVoiceActivityNotExist() {
        when(event.getCurrent()).thenReturn(voiceState);
        when(voiceState.getGuildId()).thenReturn(Snowflake.of("1"));
        when(voiceState.getUserId()).thenReturn(Snowflake.of("2"));
        when(activityRepository.getVoiceActivity(anyLong(), anyLong()))
                .thenReturn(null);

        when(event.isJoinEvent()).thenReturn(true);
        when(activityRepository.saveVoiceActivity(anyLong(), anyLong(), any())).thenReturn(voiceActivityMap);

        Mono<Void> mono = trackingService.onVoiceChannelEvent(event);
        StepVerifier.create(mono)
                .expectSubscription()
                .verifyComplete();

        verify(activityRepository, times(1)).getVoiceActivity(1L, 2L);
        verify(activityRepository, times(1)).saveVoiceActivity(eq(1L), eq(2L), argThat(arg -> arg.getActiveTime() == 0
                && arg.getJoinedAt() < System.currentTimeMillis()));
    }
}