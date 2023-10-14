package com.laa66.goodboi.voiceactivity;

import java.util.Map;

public interface VoiceChannelActivityRepository {

    Map<Long, VoiceActivity> saveVoiceActivity(long guildId, long discordId, VoiceActivity voiceActivity);

    Map<Long, VoiceActivity> getGuildVoiceActivity(long guildId);

    VoiceActivity getVoiceActivity(long guildId, long discordId);
}
