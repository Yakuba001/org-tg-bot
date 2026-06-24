package com.orgtgbot.bot.update;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    boolean canHandle(Update update, boolean isUserRegistered);
    void handle(Update update, Long chatId) throws Exception;
}
