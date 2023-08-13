package com.laa66.goodboi.config;

import com.laa66.goodboi.command.CommandFactory;
import com.laa66.goodboi.listener.EventListener;
import com.laa66.goodboi.listener.MessageCreateEventListener;
import com.laa66.goodboi.music.AudioContextRepository;
import com.laa66.goodboi.music.AudioContextCacheRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    @Value("${bot.discord.token}")
    private String discordToken;

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        GatewayDiscordClient client = DiscordClientBuilder.create(discordToken)
                .build()
                .login()
                .block();

        eventListeners.forEach(eventListener -> client.on(eventListener.getEventType())
                .flatMap(eventListener::process)
                .subscribe());

        client.onDisconnect().block();
        return client;
    }

    @Bean
    public CommandFactory commandFactory(AudioPlayerManager audioPlayerManager, AudioContextRepository audioContextRepository) {
        return new CommandFactory(audioPlayerManager, audioContextRepository);
    }

    @Bean
    public MessageCreateEventListener messageCreateEventListener(CommandFactory commandFactory) {
        return new MessageCreateEventListener(commandFactory);
    }

    @Bean
    public AudioPlayerManager audioPlayerManager() {
        final DefaultAudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        return playerManager;
    }

    @Bean
    public AudioContextRepository audioPlayerRepository(CacheManager cacheManager) {
        final Cache cache = cacheManager.getCache("audio_player");
        return new AudioContextCacheRepository(cache);
    }

}
