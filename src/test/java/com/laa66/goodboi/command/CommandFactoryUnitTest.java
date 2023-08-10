package com.laa66.goodboi.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommandFactoryUnitTest {

    @Mock
    Message message;

    @Mock
    MessageCreateEvent messageCreateEvent;

    @InjectMocks
    CommandFactory commandFactory;

    @Test
    void shouldCreatePushCommand() {
        Command command = commandFactory.create(CommandType.PUSH_MESSAGE, message);
        assertEquals(PushCommand.class, command.getClass());
    }

    @Test
    void shouldCreateInvalidCommandType() {
        assertThrows(IllegalArgumentException.class, () -> commandFactory.create(CommandType.EXIT_CHANNEL, message));
    }

    @Test
    void shouldCreateJoinCommand() {
        Command command = commandFactory.create(CommandType.JOIN_CHANNEL, messageCreateEvent);
        assertEquals(JoinCommand.class, command.getClass());
    }

}