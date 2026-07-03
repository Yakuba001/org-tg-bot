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
public class DeclineClear implements ClickableHandler {

    private final TelegramSender sender;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.DECLINE_CLEAR;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                GeneralFields.PROBEG_MENU.getDescription(),
                KeyboardFactory.buildMenuForGroup(GeneralFields.PROBEG_MENU)
        );
    }
}
