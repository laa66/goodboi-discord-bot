package com.laa66.goodboi.command;

import discord4j.core.object.entity.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommandFactoryUnitTest {

    @Mock
    Message message;

    CommandFactory commandFactory;

    @BeforeEach
    void setup() {
        commandFactory = new CommandFactory();
    }

    @Test
    void shouldCreateValidCommandType() {
        Command command = commandFactory.create(CommandType.PUSH_MESSAGE, message);
        assertEquals(PushCommand.class, command.getClass());
    }

    @Test
    void shouldCreateInvalidCommandType() {
        assertThrows(IllegalArgumentException.class, () -> commandFactory.create(CommandType.EXIT_CHANNEL, message));
    }

}