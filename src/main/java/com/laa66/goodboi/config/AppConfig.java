package com.laa66.goodboi.config;

import com.laa66.goodboi.command.CommandFactory;
import com.laa66.goodboi.discord.GuildCommandRegister;
import com.laa66.goodboi.filter.MessageValidationService;
import com.laa66.goodboi.filter.OffensiveMessageValidationService;
import com.laa66.goodboi.listener.*;
import com.laa66.goodboi.music.AudioContextRepository;
import com.laa66.goodboi.music.AudioContextCacheRepository;
import com.laa66.goodboi.user.UserRepository;
import com.laa66.goodboi.user.UserService;
import com.laa66.goodboi.user.UserServiceImpl;
import com.laa66.goodboi.voiceactivity.*;
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

    @Value("${bot.discord.dev.guild-id}")
    private String devGuildId;

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners, VoiceChannelActivityTask voiceChannelActivityTask) {
        GatewayDiscordClient client = DiscordClientBuilder.create(discordToken)
                .build()
                .login()
                .block();

        new GuildCommandRegister(client.getRestClient())
                .registerCommands(devGuildId);

        voiceChannelActivityTask.scheduleRepositoryTask(23, 59)
                .subscribe();

        eventListeners.forEach(eventListener -> client.on(eventListener.getEventType())
                .flatMap(eventListener::process)
                .subscribe());

        client.onDisconnect().block();
        return client;
    }

    @Bean
    public VoiceChannelActivityTask voiceChannelActivityTask(VoiceChannelActivityRepository voiceChannelActivityRepository) {
        return new VoiceChannelActivityTask(voiceChannelActivityRepository);
    }

    @Bean
    public MessageValidationService messageValidationService(UserService userService) {
        return new OffensiveMessageValidationService(userService);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public CommandFactory commandFactory(AudioPlayerManager audioPlayerManager,
                                         AudioContextRepository audioContextRepository,
                                         UserService userService,
                                         VoiceChannelActivityRepository voiceChannelActivityRepository) {
        return new CommandFactory(audioPlayerManager, audioContextRepository, userService, voiceChannelActivityRepository);
    }

    @Bean
    public VoiceStateUpdateEventListener voiceStateUpdateEventListener(VoiceChannelActivityTrackingService voiceChannelActivityTrackingService) {
        return new VoiceStateUpdateEventListener(voiceChannelActivityTrackingService);
    }

    @Bean
    public BanEventListener banEventListener(UserService userService) {
        return new BanEventListener(userService);
    }

    @Bean
    public UnbanEventListener unbanEventListener(UserService userService) {
        return new UnbanEventListener(userService);
    }

    @Bean
    public ApplicationCommandEventListener applicationCommandEventListener(CommandFactory commandFactory) {
        return new ApplicationCommandEventListener(commandFactory);
    }

    @Bean
    public MessageCreateEventListener messageCreateEventListener(CommandFactory commandFactory, MessageValidationService messageValidationService) {
        return new MessageCreateEventListener(commandFactory, messageValidationService);
    }

    @Bean
    public AudioPlayerManager audioPlayerManager() {
        final DefaultAudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        return playerManager;
    }

    @Bean
    public VoiceChannelActivityRepository voiceChannelActivityRepository(CacheManager cacheManager) {
        final Cache cache = cacheManager.getCache("voice_activity");
        return new VoiceChannelActivityCacheRepository(cache);
    }

    @Bean
    public VoiceChannelActivityTrackingService voiceChannelActivityTrackingService(VoiceChannelActivityRepository
                                                                                   voiceChannelActivityRepository) {
        return new VoiceChannelActivityTrackingServiceImpl(voiceChannelActivityRepository);
    }

    @Bean
    public AudioContextRepository audioContextRepository(CacheManager cacheManager) {
        final Cache cache = cacheManager.getCache("audio_player");
        return new AudioContextCacheRepository(cache);
    }

}
