package com.orgtgbot.exception.exceptions.service;

import com.orgtgbot.exception.exceptions.BotException;

public class DateIndexOutOfBoundException extends BotException {
    public DateIndexOutOfBoundException(Long chatId, String message) {
        super(chatId, message);
    }
}
