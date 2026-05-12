package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.state.UserStateService;
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
public class ProbegMenuCallback implements CallbackHandler {

    private final TelegramClient telegramClient;
    private final UserStateService userStateService;

    @Override
    public String callbackData() {
        return Buttons.PROBEG_MENU.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws TelegramApiException {
        userStateService.removeState(callbackQuery.getMessage().getChatId());

        telegramClient.execute(EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(Buttons.PROBEG_MENU.getName())
                .replyMarkup(KeyboardFactory.probegMenu())
                .build());
    }
}
