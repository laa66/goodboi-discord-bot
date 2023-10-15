package com.laa66.goodboi.listener;

import com.laa66.goodboi.command.CommandFactory;
import com.laa66.goodboi.command.CommandType;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import static com.laa66.goodboi.command.CommandType.*;

@AllArgsConstructor
public class ApplicationCommandEventListener implements EventListener<ChatInputInteractionEvent> {

    private final CommandFactory commandFactory;

    @Override
    public Class<ChatInputInteractionEvent> getEventType() {
        return ChatInputInteractionEvent.class;
    }

    @Override
    public Mono<Void> process(ChatInputInteractionEvent event) {
        CommandType commandType;
        switch (event.getCommandName()) {
            case "warns" -> commandType = WARNED_USERS;
            case "bans" -> commandType = BANNED_USERS;
            case "rude" -> commandType = RUDEST_USERS;
            case "activity" -> commandType = GUILD_VOICE_ACTIVITY;
            default -> {
                return Mono.empty();
            }
        }
        return commandFactory
                .create(commandType, event)
                .execute();
    }
}
