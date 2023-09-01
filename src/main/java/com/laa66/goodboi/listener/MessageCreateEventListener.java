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
                    String[] splitMessage = message.getContent().split(" ");
                    Command command;
                    switch (splitMessage[0]) {
                        case "!goodboi-command" -> command = commandFactory.create(COMMAND_INFO, message);
                        case "!push" -> command = commandFactory.create(PUSH_MESSAGE, message);
                        case "!zahir" -> command = commandFactory.create(ZAHIR_MESSAGE, message);
                        case "!join" -> command = commandFactory.create(JOIN_CHANNEL, event);
                        case "!play" -> command = commandFactory.create(PLAY_MUSIC, event);
                        case "!skip" -> command = commandFactory.create(SKIP_MUSIC, event);
                        case "!clean" -> command = commandFactory.create(CLEAN_SCHEDULER, event);
                        case "!res" -> command = commandFactory.create(RESUME_MUSIC, event);
                        case "!stop" -> command = commandFactory.create(STOP_MUSIC, event);
                        case "!queue" -> command = commandFactory.create(PRINT_QUEUE, event);
                        case "!exit" -> command = commandFactory.create(EXIT_CHANNEL, event);
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
