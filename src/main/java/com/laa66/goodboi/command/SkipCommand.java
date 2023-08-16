package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContextRepository;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class SkipCommand implements Command {

    private final MessageCreateEvent messageCreateEvent;
    private final AudioContextRepository contextRepository;

    @Override
    public Mono<Void> execute() {
        return Mono.justOrEmpty(messageCreateEvent.getGuildId()
                .orElseThrow()
                .asLong())
                .flatMap(guildId -> Mono.justOrEmpty(contextRepository.getContext(guildId))
                        .flatMap(context -> Mono
                                .fromRunnable(() -> context.getScheduler()
                                .skip(context.getPlayer()))))
                .then();
    }
}
