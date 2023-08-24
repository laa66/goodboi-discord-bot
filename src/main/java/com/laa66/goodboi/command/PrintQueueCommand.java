package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContextRepository;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.MessageCreateSpec;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Collection;

@AllArgsConstructor
public class PrintQueueCommand implements Command {

    MessageCreateEvent event;
    AudioContextRepository repository;

    @Override
    public Mono<Void> execute() {
        return event.getMessage()
                        .getChannel()
                        .flatMap(channel -> channel.createMessage(MessageCreateSpec
                                .builder()
                                .content("Goodboi music queue \uD83C\uDFB5 \n" + StringUtils.collectionToDelimitedString(getQueue()
                                        .stream()
                                        .map(track -> track.getInfo().title)
                                        .toList(), "", "", "\n"))
                                .build()))
                .then();
    }

    private Collection<AudioTrack> getQueue() {
        return repository.getContext(event.getGuildId().orElseThrow().asLong())
                .getScheduler()
                .getQueue();
    }
}
