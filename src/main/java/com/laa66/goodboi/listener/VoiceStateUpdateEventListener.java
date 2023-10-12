package com.laa66.goodboi.listener;

import com.laa66.goodboi.voiceactivity.VoiceChannelActivityTrackingService;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class VoiceStateUpdateEventListener implements EventListener<VoiceStateUpdateEvent> {

    private final VoiceChannelActivityTrackingService voiceChannelActivityTrackingService;

    @Override
    public Class<VoiceStateUpdateEvent> getEventType() {
        return VoiceStateUpdateEvent.class;
    }

    @Override
    public Mono<Void> process(VoiceStateUpdateEvent event) {
        return voiceChannelActivityTrackingService.onVoiceChannelEvent(event);
    }
}