package com.laa66.goodboi.voiceactivity;

import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import reactor.core.publisher.Mono;

import java.util.Optional;

@AllArgsConstructor
public class VoiceChannelActivityCacheRepository implements VoiceChannelActivityRepository {

    private final Cache cache;

    @Override
    public Mono<Void> saveVoiceActivity(long guildId, long discordId, VoiceActivity voiceActivity) {
        return Mono.empty();
    }

    @Override
    public Mono<VoiceActivity> getVoiceActivity(long guildId, long discordId) {
        String key = guildId + "#" + discordId;
        return Mono.justOrEmpty(Optional
                .ofNullable(cache)
                .map(cache -> cache.get(key, VoiceActivity.class))
                .orElse(null));
    }
}
