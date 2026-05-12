package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class MainMenuCallback implements CallbackHandler {

    private final TelegramClient telegramClient;

    @Override
    public String callbackData() {
        return Buttons.MAIN_MENU.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws TelegramApiException {
        telegramClient.execute(EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(Buttons.MAIN_MENU.getName())
                .replyMarkup(KeyboardFactory.probegMenu())
                .build());
    }
}
