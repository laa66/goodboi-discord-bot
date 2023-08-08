package com.laa66.goodboi.config;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${bot.discord.token}")
    private String discordToken;

    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        GatewayDiscordClient client = DiscordClientBuilder.create(discordToken)
                .build()
                .login()
                .block();
        client.onDisconnect().block();
        return client;
    }

}
