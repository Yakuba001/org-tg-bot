package com.orgtgbot.exception.exceptions.http;

import com.orgtgbot.exception.exceptions.BotException;

public class SendHttpRequestToGeminiException extends BotException {

    public SendHttpRequestToGeminiException(String message) {
        super(message);
    }

    public SendHttpRequestToGeminiException(String message, Throwable cause) {
        super(message, cause);
    }
}
