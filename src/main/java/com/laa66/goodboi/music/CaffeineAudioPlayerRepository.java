package com.laa66.goodboi.music;

import com.laa66.goodboi.exception.CacheEntryNotFoundException;
import com.laa66.goodboi.exception.CacheNotFoundException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;

import java.util.Optional;

@AllArgsConstructor
public class CaffeineAudioPlayerRepository implements AudioPlayerRepository {

    private final static CacheNotFoundException CACHE_NOT_FOUND_EXCEPTION = new CacheNotFoundException("Audio player cache does not exist");
    private final Cache cache;

    @Override
    public AudioPlayer getPlayer(String guildId) {
        return Optional.ofNullable(cache)
                .map(cache -> cache.get(guildId, AudioPlayer.class))
                .orElseThrow(() -> new CacheEntryNotFoundException("Audio player not found in cache for guild: " + guildId));
    }

    @Override
    public void savePlayer(String guildId, AudioPlayer player) {
        Optional.ofNullable(cache)
                .ifPresentOrElse(cache -> cache.putIfAbsent(guildId, player), () -> {
                    throw CACHE_NOT_FOUND_EXCEPTION;
                });
    }

    @Override
    public void removePlayer(String guildId) {
        Optional.ofNullable(cache)
                .ifPresentOrElse(cache -> cache.evictIfPresent(guildId), () -> {
                    throw CACHE_NOT_FOUND_EXCEPTION;
                });
    }
}
