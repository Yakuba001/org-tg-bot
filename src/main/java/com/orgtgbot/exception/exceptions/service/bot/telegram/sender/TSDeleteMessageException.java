package com.orgtgbot.exception.exceptions.service.bot.telegram.sender;

import com.orgtgbot.exception.exceptions.BotException;

public class TSDeleteMessageException extends BotException {
    public TSDeleteMessageException(Long chatId, String message, Throwable cause) {
        super(chatId, message, cause);
    }
}
