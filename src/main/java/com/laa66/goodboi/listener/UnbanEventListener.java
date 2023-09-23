package com.laa66.goodboi.listener;

import com.laa66.goodboi.user.UserService;
import discord4j.core.event.domain.guild.UnbanEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class UnbanEventListener implements EventListener<UnbanEvent> {

    private final UserService userService;

    @Override
    public Class<UnbanEvent> getEventType() {
        return UnbanEvent.class;
    }

    @Override
    public Mono<Void> process(UnbanEvent event) {
        return event.getUser()
                .asMember(event.getGuildId())
                .flatMap(userService::unban)
                .then();
    }
}
