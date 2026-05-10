package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.command.registry.CommandRegistry;
import com.orgtgbot.bot.keyboard.Buttons;
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
public class ProbegMondayCallback implements CallbackHandler {

    private final TelegramClient client;

    private final CommandRegistry registry;

    @Override
    public String callbackData() {
        return Buttons.PROBEG_MONDAY.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        try {
            client.execute(EditMessageText.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .text(" Понедельник:")
                    .replyMarkup(KeyboardFactory.probegMonday())
                    .build()
            );
        } catch (TelegramApiException e) {
            log.error("Ошибка отображения меню пробега", e);
        }
    }
}
