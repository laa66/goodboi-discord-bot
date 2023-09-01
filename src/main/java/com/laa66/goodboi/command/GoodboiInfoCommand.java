package com.laa66.goodboi.command;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GoodboiInfoCommand implements Command {

    private final Message message;

    @Override
    public Mono<Void> execute() {
        return message.getChannel()
                .flatMap(messageChannel -> messageChannel.createMessage(MessageCreateSpec.builder()
                        .content("""
                                Goodboi commands:
                                ```
                                !push  /will pop
                                !join  /join channel
                                !exit  /exit channel
                                !play <URL>  /add track to queue
                                !stop  /pause track
                                !res  /resume track
                                !skip  /play next track from queue
                                !clean  /clear queue
                                !queue  /show queue
                                !goodboi-command  /show available commands
                                ```
                                """)
                        .build()))
                .then();
    }
}
