package com.orgtgbot.exception.exceptions.bot;

import com.orgtgbot.exception.exceptions.BotException;

public class UnknownUpdateHandlerException extends BotException {
    public UnknownUpdateHandlerException(String message) {
        super(message);
    }
}
