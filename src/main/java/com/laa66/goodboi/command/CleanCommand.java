package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContextRepository;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CleanCommand implements Command {

    private final MessageCreateEvent event;
    private final AudioContextRepository repository;

    @Override
    public Mono<Void> execute() {
        return Mono.justOrEmpty(event.getGuildId()
                        .orElseThrow()
                        .asLong())
                .flatMap(guildId -> Mono.just(repository.getContext(guildId)))
                .flatMap(context -> Mono.fromRunnable(() -> context.getScheduler()
                        .clean(context.getPlayer())))
                .then();
    }
}
