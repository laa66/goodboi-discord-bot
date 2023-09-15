package com.laa66.goodboi.command;

import com.laa66.goodboi.music.AudioContextRepository;
import com.laa66.goodboi.user.UserService;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandFactory {

    private final AudioPlayerManager playerManager;
    private final AudioContextRepository contextRepository;
    private final UserService userService;

    public Command create(CommandType commandType, Object... args) {
        Command command;
        switch (commandType) {
            case COMMAND_INFO -> command = new GoodboiInfoCommand((Message) args[0]);
            case PUSH_MESSAGE, ZAHIR_MESSAGE -> command = new PushCommand((Message) args[0]);
            case JOIN_CHANNEL -> command = new JoinCommand((MessageCreateEvent) args[0], playerManager, contextRepository);
            case PLAY_MUSIC -> command = new PlayCommand((MessageCreateEvent) args[0], playerManager, contextRepository);
            case SKIP_MUSIC -> command = new SkipCommand((MessageCreateEvent) args[0], contextRepository);
            case STOP_MUSIC -> command = new StopCommand((MessageCreateEvent) args[0], contextRepository);
            case RESUME_MUSIC -> command = new ResumeCommand((MessageCreateEvent) args[0], contextRepository);
            case CLEAN_SCHEDULER -> command = new CleanCommand((MessageCreateEvent) args[0], contextRepository);
            case PRINT_QUEUE -> command = new PrintQueueCommand((MessageCreateEvent) args[0], contextRepository);
            case EXIT_CHANNEL -> command = new ExitCommand((MessageCreateEvent) args[0]);
            case BANNED_USERS -> command = new FindBannedCommand((MessageCreateEvent) args[0], userService);
            default -> throw new IllegalArgumentException("Cannot create command, invalid command type");
        }
        return command;
    }

}