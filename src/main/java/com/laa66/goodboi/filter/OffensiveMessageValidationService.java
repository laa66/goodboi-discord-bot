package com.laa66.goodboi.filter;

import com.laa66.goodboi.user.UserService;
import discord4j.core.object.entity.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

@Slf4j
@AllArgsConstructor
public class OffensiveMessageValidationService implements MessageValidationService {

    private final UserService userService;

    @Override
    public Mono<Void> filterMessage(Message message) {
        return validateMessage(message.getContent())
                .flatMap(isOffensive -> isOffensive
                        ? message.getAuthorAsMember().flatMap(userService::warn)
                        : Mono.empty());
    }

    private Mono<Boolean> validateMessage(String content) {
        return Mono.just(getBadWords()
                .stream()
                .anyMatch(word -> content
                        .toLowerCase()
                        .contains(word.toLowerCase())));
    }

    private Collection<String> getBadWords() {
        Collection<String> badWords = null;
        try {
            byte[] bytes = new ClassPathResource(Path.of("validate/badwords.txt")
                    .toString())
                    .getInputStream()
                    .readAllBytes();
            badWords = StringUtils.commaDelimitedListToSet(new String(bytes));
        } catch (IOException e) {
            log.error("Error while reading bad words list from file", e);
        }
        return badWords;
    }


}
