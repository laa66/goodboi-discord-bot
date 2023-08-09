package com.laa66.goodboi.command;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class PushCommand implements Command {

    private final Message message;

    @Override
    public Mono<Void> execute() {
        return message.getChannel()
                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                        .content(message
                                .getContent()
                                .equalsIgnoreCase("!push") ? "pop" : "Samo mięso sosy mieszane!")
                        .build()))
                .then();
    }
}
