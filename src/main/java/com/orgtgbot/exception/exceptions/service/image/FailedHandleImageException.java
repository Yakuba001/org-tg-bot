package com.orgtgbot.exception.exceptions.service.image;

import com.orgtgbot.exception.exceptions.BotException;

public class FailedHandleImageException extends BotException {
    public FailedHandleImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedHandleImageException(Long chatId, String message, Throwable cause) {
        super(chatId, message, cause);
    }
}
