package com.laa66.goodboi.listener;

import com.laa66.goodboi.command.Command;
import com.laa66.goodboi.command.CommandFactory;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import static com.laa66.goodboi.command.CommandType.*;

@AllArgsConstructor
public class MessageCreateEventListener implements EventListener<MessageCreateEvent> {

    private final CommandFactory commandFactory;

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> process(MessageCreateEvent event) {
        return filterMessage(event)
                .flatMap(message -> {
                    Command command;
                    switch (message.getContent()) {
                        case "!push" -> command = commandFactory.create(PUSH_MESSAGE, message);
                        case "!zahir" -> command = commandFactory.create(ZAHIR_MESSAGE, message);
                        default -> {
                            return Mono.empty();
                        }
                    }
                    return command.execute();
                });
    }

    private Mono<Message> filterMessage(MessageCreateEvent event) {
        return Mono.just(event.getMessage())
                .filter(message -> !message.getAuthor().orElseThrow().isBot())
                .filter(message -> !message.isPinned())
                .filter(message -> message.getContent().startsWith("!"));
    }
}
