package com.orgtgbot.exception.exceptions.callback;

import com.orgtgbot.exception.exceptions.BotException;

public class DispatcherHandleException extends BotException {
    public DispatcherHandleException(Long chatId, String message) {
        super(chatId, message);
    }
}
