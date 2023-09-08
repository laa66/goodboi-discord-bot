package com.laa66.goodboi.discord;

import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return Flux.fromStream(getJsonPaths()
                .stream())
                .flatMap(this::readJsonCommand)
                .filter(Objects::nonNull);
    }

    private Mono<ApplicationCommandRequest> readJsonCommand(Path path) {
        return Mono.fromCallable(() -> {
            ApplicationCommandRequest commandRequest = null;
            try (InputStream resourceStream = new ClassPathResource(path.toString()).getInputStream()) {
                commandRequest = mapper.getObjectMapper()
                        .readValue(resourceStream, ApplicationCommandRequest.class);
            } catch (IOException e) {
                log.error("Error while reading json commands", e);
            }
            return commandRequest;
        });
    }

    private Set<Path> getJsonPaths() {
        Set<Path> paths = null;
        try (Stream<Path> pathStream = Files.list(Path.of("src/main/resources/commands"))) {
            paths = pathStream
                    .filter(file -> !Files.isDirectory(file))
                    .map(path -> Path.of(path.getParent().getFileName() + "/" + path.getFileName()))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.error("Error while reading json paths", e);
        }
        return paths;
    }

}
