package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.callback.state.UserStateService;
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
    private final UserStateService userStateService;

    @Override
    public String callbackData() {
        return Buttons.PROBEG_MONDAY.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        try {
            client.execute(EditMessageText.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .text(" Понедельник:")
                    .replyMarkup(KeyboardFactory.probegMonday())
                    .build()
            );
            userStateService.setState(chatId, "WAITING_PROBEG_MONDAY");
            userStateService.setMessageId(chatId, callbackQuery.getMessage().getMessageId());
        } catch (TelegramApiException e) {
            log.error("Ошибка отображения меню пробега", e);
        }
    }
}
