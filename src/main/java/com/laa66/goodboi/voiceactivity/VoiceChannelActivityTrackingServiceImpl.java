package com.laa66.goodboi.voiceactivity;

import discord4j.core.event.domain.VoiceStateUpdateEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;


@AllArgsConstructor
public class VoiceChannelActivityTrackingServiceImpl implements VoiceChannelActivityTrackingService {

    private final VoiceChannelActivityRepository voiceChannelActivityRepository;

    @Override
    public Mono<Void> onVoiceChannelEvent(VoiceStateUpdateEvent event) {
        return voiceChannelActivityRepository.getVoiceActivity(event.getCurrent()
                                .getGuildId()
                                .asLong(),
                        event.getCurrent()
                                .getUserId()
                                .asLong())
                .switchIfEmpty(Mono.just(new VoiceActivity(System.currentTimeMillis(), 0L)))
                .flatMap(voiceActivity -> {
                    if (event.isJoinEvent()) voiceActivity.setJoinedAt(System.currentTimeMillis());
                    else if (event.isLeaveEvent()) voiceActivity
                            .setActiveTime(voiceActivity.getActiveTime() + (System.currentTimeMillis() - voiceActivity.getJoinedAt()));
                    return Mono.just(voiceActivity);
                })
                .flatMap(voiceActivity -> voiceChannelActivityRepository
                        .saveVoiceActivity(event.getCurrent()
                                        .getGuildId()
                                        .asLong(),
                                event.getCurrent()
                                        .getUserId()
                                        .asLong(),
                            voiceActivity));
    }
}
