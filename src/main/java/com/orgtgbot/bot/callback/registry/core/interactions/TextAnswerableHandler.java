package com.orgtgbot.bot.callback.registry.core.interactions;

import com.orgtgbot.bot.TelegramSender;

public interface TextAnswerableHandler extends CallbackHandler {

    void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender);
}
