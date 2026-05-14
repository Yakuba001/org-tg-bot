package com.orgtgbot.bot.command;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.DateService;
import com.orgtgbot.service.GeneralService;
import com.orgtgbot.service.ProbegService;
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
    private final ProbegService probegService;
    private final DateService dateService;
    private final GeneralService generalService;

    public void execute(Update update) throws TelegramApiException {
        probegService.firstStart();
        dateService.firstStart();
        generalService.firstStart();

        telegramClient.execute(SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(Buttons.MAIN_MENU.getName())
                .replyMarkup(KeyboardFactory.mainMenu())
                .build());
    }
}
