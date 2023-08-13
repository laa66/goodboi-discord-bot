package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContextRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class PlayCommand implements Command {

    private final MessageCreateEvent messageCreateEvent;
    private final AudioPlayerManager playerManager;
    private final AudioContextRepository contextRepository;

    @Override
    public Mono<Void> execute() {
        return Mono.justOrEmpty(messageCreateEvent.getMessage().getContent())
                .map(content -> content.split(" "))
                .flatMapMany(Flux::fromArray)
                .skip(1)
                .flatMap(link -> {
                    long guildId = messageCreateEvent.getGuildId()
                            .orElseThrow()
                            .asLong();
                    return Mono.justOrEmpty(contextRepository.getContext(guildId))
                            .flatMap(context -> Mono.fromRunnable(() -> playerManager
                                    .loadItem(link, context.getLoadResultHandler())));
                }).then();
    }
}
