package com.laa66.goodboi.voiceactivity;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class VoiceChannelActivityTrackingServiceImpl implements VoiceChannelActivityTrackingService {

    private final VoiceChannelActivityRepository voiceChannelActivityRepository;

    @Override
    public Mono<Void> onVoiceChannelEvent(VoiceStateUpdateEvent event) {
        return null;
    }
}
