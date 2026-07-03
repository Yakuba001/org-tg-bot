package com.orgtgbot.exception.exceptions.service;

import com.orgtgbot.exception.exceptions.BotException;

public class DayNotFoundException extends BotException {
    public DayNotFoundException(Long chatId, String message) {
        super(chatId, message);
    }
}
