package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.TelegramSender;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackHandler {

    GeneralFields callbackData();
    void handle(CallbackQuery callbackQuery) throws Exception;
    void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender);
}
