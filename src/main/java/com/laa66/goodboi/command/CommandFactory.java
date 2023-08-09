package com.laa66.goodboi.command;

import discord4j.core.object.entity.Message;

public class CommandFactory {

    public Command create(CommandType commandType, Object... args) {
        Command command;
        switch (commandType) {
            case PUSH_MESSAGE, ZAHIR_MESSAGE -> command = new PushCommand((Message) args[0]);
            default -> throw new IllegalArgumentException("Cannot create command, invalid command type");
        }
        return command;
    }

}