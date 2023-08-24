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

    @Test
    void shouldCreatePlayCommand() {
        Command command = commandFactory.create(CommandType.PLAY_MUSIC, messageCreateEvent);
        assertEquals(PlayCommand.class, command.getClass());
    }

    @Test
    void shouldCreateSkipCommand() {
        Command command = commandFactory.create(CommandType.SKIP_MUSIC, messageCreateEvent);
        assertEquals(SkipCommand.class, command.getClass());
    }

    @Test
    void shouldCreateStopCommand() {
        Command command = commandFactory.create(CommandType.STOP_MUSIC, messageCreateEvent);
        assertEquals(StopCommand.class, command.getClass());
    }

    @Test
    void shouldCreateCleanCommand() {
        Command command = commandFactory.create(CommandType.CLEAN_SCHEDULER, messageCreateEvent);
        assertEquals(CleanCommand.class, command.getClass());
    }

    @Test
    void shouldCreateQueueCommand() {
        Command command = commandFactory.create(CommandType.PRINT_QUEUE, messageCreateEvent);
        assertEquals(PrintQueueCommand.class, command.getClass());
    }

}