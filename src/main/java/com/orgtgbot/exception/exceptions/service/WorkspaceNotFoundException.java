package com.orgtgbot.exception.exceptions.service;

import com.orgtgbot.exception.exceptions.BotException;

public class WorkspaceNotFoundException extends BotException {
    public WorkspaceNotFoundException(Long chatId, String message) {
        super(chatId, message);
    }
}
