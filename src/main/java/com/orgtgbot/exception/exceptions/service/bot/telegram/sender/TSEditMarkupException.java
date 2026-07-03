package com.orgtgbot.exception.exceptions.service.bot.telegram.sender;

import com.orgtgbot.exception.exceptions.BotException;

public class TSEditMarkupException extends BotException {
    public TSEditMarkupException(Long chatId, String message, Throwable cause) {
        super(chatId, message, cause);
    }
}
