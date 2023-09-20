package com.laa66.goodboi.command;

import com.laa66.goodboi.user.User;
import com.laa66.goodboi.user.UserService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionFollowupCreateSpec;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class FindWarnedCommand implements Command {

    private final ChatInputInteractionEvent event;
    private final UserService userService;

    @Override
    public Mono<Void> execute() {
        return event.deferReply()
                .then(event.createFollowup(InteractionFollowupCreateSpec.builder()
                                .content("List of warned users\n" + StringUtils.collectionToDelimitedString(
                                        userService.findAllUsers(event.getInteraction()
                                                        .getGuildId()
                                                        .orElseThrow()
                                                        .asLong())
                                                .map(User::getUsername)
                                                .toStream()
                                                .toList(), "", "", "\n"))
                        .build()))
                .then();
    }
}
