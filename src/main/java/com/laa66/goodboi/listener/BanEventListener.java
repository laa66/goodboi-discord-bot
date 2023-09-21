package com.laa66.goodboi.listener;

import com.laa66.goodboi.user.UserService;
import discord4j.core.event.domain.guild.BanEvent;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class BanEventListener implements EventListener<BanEvent> {

    private final UserService userService;

    @Override
    public Class<BanEvent> getEventType() {
        return BanEvent.class;
    }

    @Override
    public Mono<Void> process(BanEvent event) {
        return event.getUser()
                .asMember(event.getGuildId())
                .flatMap(userService::ban)
                .then();
    }
}
