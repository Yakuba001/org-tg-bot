package com.orgtgbot.exception.exceptions.service.voice;

import com.orgtgbot.exception.exceptions.BotException;

public class FailedHandleVoiceException extends BotException {
    public FailedHandleVoiceException(Long chatId, String message, Throwable cause) {
        super(chatId, message, cause);
    }
}
