package com.laa66.goodboi.command;

import reactor.core.publisher.Mono;

public interface Command {

    Mono<Void> execute();
}
