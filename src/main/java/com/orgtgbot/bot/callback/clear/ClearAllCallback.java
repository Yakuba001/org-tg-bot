package com.orgtgbot.bot.callback.clear;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.interactions.ClickableHandler;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class ClearAllCallback implements ClickableHandler {

    private final TelegramSender sender;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.CLEAR_ALL;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackData().getDescription(),
                KeyboardFactory.buildMenuForGroup(callbackData())
        );
    }
}
