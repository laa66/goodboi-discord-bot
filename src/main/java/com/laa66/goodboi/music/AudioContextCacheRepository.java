package com.laa66.goodboi.music;

import com.laa66.goodboi.exception.CacheNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;

import java.util.Optional;

@AllArgsConstructor
public class AudioContextCacheRepository implements AudioContextRepository {

    private final static CacheNotFoundException CACHE_NOT_FOUND_EXCEPTION = new CacheNotFoundException("Audio context cache does not exist");
    private final Cache cache;

    @Override
    public AudioContext getContext(long guildId) {
        return Optional.ofNullable(cache)
                .map(cache -> cache.get(guildId, AudioContext.class))
                .orElse(null);
    }

    @Override
    public void saveContext(long guildId, AudioContext context) {
        Optional.ofNullable(cache)
                .ifPresentOrElse(cache -> cache.putIfAbsent(guildId, context), () -> {
                    throw CACHE_NOT_FOUND_EXCEPTION;
                });
    }

    @Override
    public void removeContext(long guildId) {
        Optional.ofNullable(cache)
                .ifPresentOrElse(cache -> cache.evictIfPresent(guildId), () -> {
                    throw CACHE_NOT_FOUND_EXCEPTION;
                });
    }
}
