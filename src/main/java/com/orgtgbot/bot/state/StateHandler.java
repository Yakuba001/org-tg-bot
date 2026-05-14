package com.orgtgbot.bot.state;

import com.orgtgbot.bot.TelegramSender;

public interface StateHandler {

    UserState getSupportedHandle();
    void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender);
}
