package com.orgtgbot.exception.exceptions.service.gemini;

import com.orgtgbot.exception.exceptions.BotException;

public class DeserializeGeminiResponse extends BotException {
    public DeserializeGeminiResponse(String message, Throwable cause) {
        super(message, cause);
    }
}
