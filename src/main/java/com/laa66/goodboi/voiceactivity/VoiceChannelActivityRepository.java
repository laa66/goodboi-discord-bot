package com.laa66.goodboi.voiceactivity;

import reactor.core.publisher.Mono;

public interface VoiceChannelActivityRepository {

    Mono<Void> saveVoiceActivity(long guildId, long discordId, VoiceActivity voiceActivity);
    Mono<VoiceActivity> getVoiceActivity(long guildId, long discordId);

}
