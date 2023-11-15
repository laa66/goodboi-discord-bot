package com.laa66.goodboi.discord;

import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@AllArgsConstructor
public class GuildCommandRegister {

    private final RestClient client;
    private final JacksonResources mapper = JacksonResources.create();

    public void registerCommands(String devGuildId) {
        Optional<Long> appId = Optional.ofNullable(client.getApplicationId().block());
        appId.map(id -> buildGuildCommands()
                        .flatMap(command -> client.getApplicationService()
                                .createGuildApplicationCommand(id, Long.parseLong(devGuildId), command))
                        .subscribe())
                .orElseThrow();
    }

    private Flux<ApplicationCommandRequest> buildGuildCommands() {
        return getCommandResources()
                .flatMap(this::readJsonCommand)
                .filter(Objects::nonNull);
    }

    private Mono<ApplicationCommandRequest> readJsonCommand(Resource resource) {
        return Mono.fromCallable(() -> {
            ApplicationCommandRequest commandRequest = null;
            try (InputStream resourceStream = resource.getInputStream()) {
                commandRequest = mapper.getObjectMapper()
                        .readValue(resourceStream, ApplicationCommandRequest.class);
            } catch (IOException e) {
                log.error("Error while reading json commands", e);
            }
            return commandRequest;
        });
    }

    private Flux<Resource> getCommandResources() {
        Flux<Resource> commandResources = null;
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:commands/*.json");
            commandResources = Flux.fromArray(resources);
        } catch (IOException e) {
            log.error("Error while reading commands resources");
        }

        return commandResources;
    }

}
