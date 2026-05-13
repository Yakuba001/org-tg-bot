package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.keyboard.Buttons;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackHandler {

    String callbackData();
    void handle(CallbackQuery callbackQuery, Buttons button) throws Exception;
}
