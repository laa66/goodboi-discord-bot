package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContextRepository;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.PartialMember;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.VoiceConnection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class ExitCommand implements Command {

    private final MessageCreateEvent event;
    private final AudioContextRepository audioContextRepository;

    @Override
    public Mono<Void> execute() {
        return Mono.justOrEmpty(event.getMember().orElseThrow())
                .flatMap(PartialMember::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(VoiceChannel::getVoiceConnection)
                .flatMap(VoiceConnection::disconnect)
                .doOnError(e ->
                    log.error("Cannot disconnect from Voice Channel", e)
                ).then(Mono.fromRunnable(() -> audioContextRepository.removeContext(event
                        .getGuildId()
                        .orElseThrow()
                        .asLong())));
    }
}