package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContextRepository;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandFactory {

    private final AudioPlayerManager playerManager;
    private final AudioContextRepository contextRepository;

    public Command create(CommandType commandType, Object... args) {
        Command command;
        switch (commandType) {
            case PUSH_MESSAGE, ZAHIR_MESSAGE -> command = new PushCommand((Message) args[0]);
            case JOIN_CHANNEL -> command = new JoinCommand((MessageCreateEvent) args[0], playerManager, contextRepository);
            case PLAY_MUSIC -> command = new PlayCommand((MessageCreateEvent) args[0], playerManager, contextRepository);
            case SKIP_MUSIC -> command = new SkipCommand((MessageCreateEvent) args[0], contextRepository);
            case STOP_MUSIC -> command = new StopCommand((MessageCreateEvent) args[0], contextRepository);
            default -> throw new IllegalArgumentException("Cannot create command, invalid command type");
        }
        return command;
    }

}