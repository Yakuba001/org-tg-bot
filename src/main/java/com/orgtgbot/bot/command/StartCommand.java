package com.orgtgbot.bot.command;

import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
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

    public void execute(Update update) throws TelegramApiException {
        telegramClient.execute(SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(GeneralFields.MAIN_MENU.getDescription())
                .replyMarkup(KeyboardFactory.buildMenuForGroup(GeneralFields.MAIN_MENU))
                .build());
    }
}
