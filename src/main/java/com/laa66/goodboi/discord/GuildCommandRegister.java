package com.laa66.goodboi.discord;

import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import discord4j.rest.RestClient;
import reactor.core.publisher.Flux;

import java.util.Optional;

public class GuildCommandRegister {

    RestClient client;

    public GuildCommandRegister(RestClient client) {
        this.client = client;
    }

    public void registerCommands(String devGuildId) {
        Optional<Long> appId = Optional.ofNullable(client.getApplicationId().block());
        appId.map(id -> buildGuildCommands()
                        .flatMap(command -> client.getApplicationService()
                                .createGuildApplicationCommand(id, Long.parseLong(devGuildId), command))
                        .subscribe())
                .orElseThrow();
    }

    private Flux<ImmutableApplicationCommandRequest> buildGuildCommands() {
        return Flux.just(
                ApplicationCommandRequest.builder()
                        .name("warns")
                        .description("List of all warns")
                        .build(),
                ApplicationCommandRequest.builder()
                        .name("bans")
                        .description("List of all bans")
                        .build(),
                ApplicationCommandRequest.builder()
                        .name("rude")
                        .description("Show 10 most rude users")
                        .build()
        );
    }

}
