package com.orgtgbot.bot.command;

import com.orgtgbot.bot.keyboard.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class StartCommand implements CommandHandler {

    private final TelegramClient telegramClient;

    @Override
    public String name() { return "start"; }

    @Override
    public String execute(Update update) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .replyMarkup(KeyboardFactory.mainMenu())
                    .build());
        } catch (TelegramApiException e) {
            return "❌ Ошибка отображения меню.";
        }
        return "";
    }
}
