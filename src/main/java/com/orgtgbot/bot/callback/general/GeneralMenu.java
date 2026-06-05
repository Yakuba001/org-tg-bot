package com.orgtgbot.bot.callback.general;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class GeneralMenu implements CallbackHandler {

    private final TelegramSender sender;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.GENERAL;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackData().getDescription(),
                KeyboardFactory.dynamicBack(callbackData())
        );
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        sender.editMarkup(chatId, botMenuId, callbackData().getDescription(),
                KeyboardFactory.buildMenuForGroup(callbackData()));
    }
}
