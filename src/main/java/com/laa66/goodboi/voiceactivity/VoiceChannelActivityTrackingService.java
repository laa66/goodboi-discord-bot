package com.laa66.goodboi.voiceactivity;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import reactor.core.publisher.Mono;

public interface VoiceChannelActivityTrackingService {

    Mono<Void> onVoiceChannelEvent(VoiceStateUpdateEvent event);

}
