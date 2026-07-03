package com.orgtgbot.exception.exceptions.service.bot.telegram.sender;

import com.orgtgbot.exception.exceptions.BotException;

public class TSSendMessageException extends BotException {
    public TSSendMessageException(Long chatId, String message, Throwable cause) {
        super(chatId, message, cause);
    }
}
