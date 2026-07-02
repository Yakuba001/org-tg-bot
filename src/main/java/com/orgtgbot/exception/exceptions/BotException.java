package com.orgtgbot.exception.exceptions;

import lombok.Getter;

@Getter
public class BotException extends RuntimeException {

    private final Long chatId;

    public BotException(String message) {
        super(message);
        this.chatId = null;
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
        this.chatId = null;
    }

    public BotException(Long chatId, String message) {
        super(message);
        this.chatId = chatId;
    }

    public BotException(Long chatId, String message, Throwable cause) {
        super(message, cause);
        this.chatId = chatId;
    }
}
