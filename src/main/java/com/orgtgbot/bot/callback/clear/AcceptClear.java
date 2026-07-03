package com.orgtgbot.bot.callback.clear;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.interactions.ClickableHandler;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.services.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class AcceptClear implements ClickableHandler {

    private final TelegramSender sender;
    private final ProbegService probegService;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.ACCEPT_CLEAR;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        probegService.clearAll(chatId);

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                "Данные очищены!",
                KeyboardFactory.buildMenuForGroup(GeneralFields.PROBEG_MENU)
        );
    }
}
