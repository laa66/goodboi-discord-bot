package com.laa66.goodboi.command;

import com.laa66.goodboi.voiceactivity.VoiceChannelActivityRepository;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionFollowupCreateSpec;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class FindVoiceActivityCommand implements Command {

    private final ChatInputInteractionEvent event;
    private final VoiceChannelActivityRepository voiceChannelActivityRepository;

    @Override
    public Mono<Void> execute() {
        return event.deferReply()
                .then(event.createFollowup(InteractionFollowupCreateSpec.builder()
                        .content("Guild Voice channels users activity\n" + StringUtils.collectionToDelimitedString(
                                Optional.ofNullable(voiceChannelActivityRepository.getGuildVoiceActivity(event.getInteraction()
                                                .getGuildId()
                                                .orElseThrow()
                                                .asLong()))
                                        .map(voiceActivityMap -> voiceActivityMap
                                                .values()
                                                .stream()
                                                .map(voiceActivity -> voiceActivity.getUsername()
                                                        + " "
                                                        + TimeUnit.MILLISECONDS.toMinutes(voiceActivity.getActiveTime()) + " min")
                                                .toList())
                                        .orElse(null)
                                       , "", "", "\n"))
                        .build()))
                .then();
    }
}