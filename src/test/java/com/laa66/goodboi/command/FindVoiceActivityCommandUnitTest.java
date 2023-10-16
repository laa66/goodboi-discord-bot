package com.laa66.goodboi.command;

import com.laa66.goodboi.voiceactivity.VoiceActivity;
import com.laa66.goodboi.voiceactivity.VoiceChannelActivityRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.Interaction;
import discord4j.core.spec.InteractionCallbackSpecDeferReplyMono;
import discord4j.core.spec.InteractionFollowupCreateSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindVoiceActivityCommandUnitTest {

    @Mock
    InteractionCallbackSpecDeferReplyMono callbackSpecDeferReplyMono;

    @Mock
    ChatInputInteractionEvent event;

    @Mock
    VoiceChannelActivityRepository repository;

    @Mock
    Interaction interaction;

    @Mock
    VoiceActivity activity1;

    @Mock
    VoiceActivity activity2;

    Map<Long, VoiceActivity> guildVoiceActivityMap;

    @BeforeEach
    void setup() {
        guildVoiceActivityMap = new HashMap<>();
        guildVoiceActivityMap.put(1L, activity1);
        guildVoiceActivityMap.put(2L, activity2);
        when(activity1.getUsername()).thenReturn("user1");
        when(activity2.getUsername()).thenReturn("user2");
        when(activity1.getActiveTime()).thenReturn(500L);
        when(activity2.getActiveTime()).thenReturn(2000L);
    }

    @Test
    void shouldExecute() {
        when(event.getInteraction()).thenReturn(interaction);
        when(interaction.getGuildId()).thenReturn(Optional.of(Snowflake.of(313L)));
        when(repository.getGuildVoiceActivity(anyLong()))
                .thenReturn(guildVoiceActivityMap);
        when(callbackSpecDeferReplyMono.then(any())).thenReturn(Mono.empty());
        when(event.deferReply()).thenReturn(callbackSpecDeferReplyMono);

        FindVoiceActivityCommand command = new FindVoiceActivityCommand(event, repository);
        StepVerifier.create(command.execute())
                .expectSubscription()
                .verifyComplete();

        verify(event, times(1)).createFollowup(any(InteractionFollowupCreateSpec.class));
        verify(repository, times(1)).getGuildVoiceActivity(313L);
    }
}