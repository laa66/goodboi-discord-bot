package com.laa66.goodboi.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.PartialMember;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.VoiceConnection;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ExitCommand implements Command {

    private final MessageCreateEvent event;

    @Override
    public Mono<Void> execute() {
        return Mono.just(event.getMember().orElseThrow())
                .flatMap(PartialMember::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(VoiceChannel::getVoiceConnection)
                .flatMap(VoiceConnection::disconnect);
    }
}