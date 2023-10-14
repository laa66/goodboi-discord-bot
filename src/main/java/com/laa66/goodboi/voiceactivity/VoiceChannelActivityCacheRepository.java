package com.laa66.goodboi.voiceactivity;

import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;

import java.util.*;

@AllArgsConstructor
public class VoiceChannelActivityCacheRepository implements VoiceChannelActivityRepository {

    private final Cache cache;

    @Override
    @CachePut(value = "#voice_activity", key = "#guild_id")
    public Map<Long, VoiceActivity> saveVoiceActivity(long guildId, long discordId, VoiceActivity voiceActivity) {
        Map<Long, VoiceActivity> guildVoiceActivity = getGuildVoiceActivity(guildId);
        guildVoiceActivity.put(discordId, voiceActivity);
        return guildVoiceActivity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, VoiceActivity> getGuildVoiceActivity(long guildId) {
        return cache.get(guildId, Map.class);
    }

    @Override
    public VoiceActivity getVoiceActivity(long guildId, long discordId) {
        Map<Long, VoiceActivity> guildVoiceActivityMap = getGuildVoiceActivity(guildId);
        return Optional.ofNullable(guildVoiceActivityMap)
                .map(voiceActivityMap -> voiceActivityMap.get(discordId))
                .orElseGet(() -> {
                   cache.put(guildId, new HashMap<Long, VoiceActivity>());
                   return null;
                });
    }
}
