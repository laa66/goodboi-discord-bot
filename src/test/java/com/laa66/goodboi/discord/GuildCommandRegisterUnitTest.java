package com.laa66.goodboi.discord;

import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuildCommandRegisterUnitTest {

    @Mock
    RestClient client;

    @Mock
    ApplicationService applicationService;

    @Mock
    ApplicationCommandData commandData;

    @InjectMocks
    GuildCommandRegister guildCommandRegister;

    @Test
    void shouldRegisterCommandsAppIdNotNull() {
        when(client.getApplicationId()).thenReturn(Mono.just(23L));
        when(client.getApplicationService()).thenReturn(applicationService);
        when(applicationService.createGuildApplicationCommand(anyLong(), anyLong(), any()))
                .thenReturn(Mono.just(commandData));
        guildCommandRegister.registerCommands("232");

        verify(client, times(1)).getApplicationId();
        verify(client, times(4)).getApplicationService();
        verify(applicationService, times(3))
                .createGuildApplicationCommand(eq(23L), eq(232L), argThat(
                        commandRequest -> commandRequest.name().equals("bans")
                        || commandRequest.name().equals("warns")
                        || commandRequest.name().equals("rude")
                ));
    }

    @Test
    void shouldRegisterCommandsAppIdNull() {
        when(client.getApplicationId()).thenReturn(Mono.empty());
        assertThrows(NoSuchElementException.class, () -> guildCommandRegister.registerCommands("232"));
    }
}