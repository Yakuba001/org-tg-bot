package com.orgtgbot.exception.exceptions.service.bot.telegram.sender;

import com.orgtgbot.exception.exceptions.BotException;

public class TSSendDocumentException extends BotException {
    public TSSendDocumentException(Long chatId, String message, Throwable cause) {
        super(chatId, message, cause);
    }
}
