package com.orgtgbot.bot.command;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.exception.exceptions.service.bot.telegram.client.TelegramClientExecutorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class StartCommand {

    private final TelegramClient telegramClient;

    public void execute(Update update) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(GeneralFields.MAIN_MENU.getDescription())
                    .replyMarkup(KeyboardFactory.buildMenuForGroup(GeneralFields.MAIN_MENU))
                    .build());
        } catch (TelegramApiException e) {
            throw new TelegramClientExecutorException("Telegram client can`t execute start command logic", e);
        }
    }
}
