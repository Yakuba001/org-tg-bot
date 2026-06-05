package com.orgtgbot.bot.callback.clear;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.services.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class AcceptClear implements CallbackHandler {

    private final TelegramSender sender;
    private final ProbegService probegService;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.ACCEPT_CLEAR;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        probegService.clearAll();

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackData().getDescription(),
                KeyboardFactory.buildMenuForGroup(callbackData())
        );
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        sender.editMarkup(chatId, botMenuId, callbackData().getDescription(),
                KeyboardFactory.buildMenuForGroup(callbackData()));
    }
}
