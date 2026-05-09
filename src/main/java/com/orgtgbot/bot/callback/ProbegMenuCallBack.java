package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.keyboard.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProbegMenuCallBack implements CallbackHandler {

    private final TelegramClient telegramClient;

    @Override
    public String callbackData() { return KeyboardFactory.PROBEG_MENU; }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        try {
            telegramClient.execute(EditMessageText.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .text(" Меню пробега:")
                    .replyMarkup(KeyboardFactory.probegMenu())
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отображения меню пробега", e);
        }
    }
}
