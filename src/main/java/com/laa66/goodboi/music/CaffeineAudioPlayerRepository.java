package com.laa66.goodboi.music;

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
    public AudioPlayer getPlayer(long guildId) {
        return Optional.ofNullable(cache)
                .map(cache -> cache.get(guildId, AudioPlayer.class))
                .orElse(null);
    }

    @Override
    public void savePlayer(long guildId, AudioPlayer player) {
        Optional.ofNullable(cache)
                .ifPresentOrElse(cache -> cache.putIfAbsent(guildId, player), () -> {
                    throw CACHE_NOT_FOUND_EXCEPTION;
                });
    }

    @Override
    public void removePlayer(long guildId) {
        Optional.ofNullable(cache)
                .ifPresentOrElse(cache -> cache.evictIfPresent(guildId), () -> {
                    throw CACHE_NOT_FOUND_EXCEPTION;
                });
    }
}
