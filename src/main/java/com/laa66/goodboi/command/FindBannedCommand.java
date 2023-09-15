package com.laa66.goodboi.command;

import com.laa66.goodboi.user.User;
import com.laa66.goodboi.user.UserService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.MessageCreateSpec;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class FindBannedCommand implements Command {

    private final MessageCreateEvent event;
    private final UserService userService;

    @Override
    public Mono<Void> execute() {
        return event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                        .content("List of banned users\n" + StringUtils.collectionToDelimitedString(
                                userService.findBannedUsers(event.getGuildId()
                                                .orElseThrow()
                                                .asLong())
                                        .map(User::getUsername)
                                        .toStream()
                                        .toList(), "", "", "\n"))
                        .build()))
                .then();
    }
}
