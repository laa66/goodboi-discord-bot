package com.laa66.goodboi.config;

import com.laa66.goodboi.command.CommandFactory;
import com.laa66.goodboi.listener.EventListener;
import com.laa66.goodboi.listener.MessageCreateEventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.beans.factory.annotation.Value;
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
    public CommandFactory commandFactory() {
        return new CommandFactory();
    }

    @Bean
    public MessageCreateEventListener messageCreateEventListener(CommandFactory commandFactory) {
        return new MessageCreateEventListener(commandFactory);
    }

}
