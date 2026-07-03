package com.orgtgbot.exception.exceptions.service;

import com.orgtgbot.exception.exceptions.BotException;

public class ExcelGeneratorException extends BotException {
    public ExcelGeneratorException(Long chatId, String message) {
        super(chatId, message);
    }
}
