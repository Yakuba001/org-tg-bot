package com.orgtgbot.bot.callback.registry.core.interactions;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface ClickableHandler extends CallbackHandler {

    void handle(CallbackQuery callbackQuery);
}
