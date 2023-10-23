package com.laa66.goodboi.voiceactivity;

import reactor.core.publisher.Flux;

import java.util.Map;

public interface VoiceChannelActivityRepository {

    Map<Long, VoiceActivity> saveVoiceActivity(long guildId, long discordId, VoiceActivity voiceActivity);

    Map<Long, VoiceActivity> getGuildVoiceActivity(long guildId);

    VoiceActivity getVoiceActivity(long guildId, long discordId);

    void clear();
}
