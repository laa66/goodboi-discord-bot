package com.laa66.goodboi.filter;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface MessageValidationService {

    Mono<Message> filterMessage(Message message);
}
