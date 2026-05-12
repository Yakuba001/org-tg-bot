package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.state.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class SetMondayTotalProbeg implements CallbackHandler {

    private final TelegramClient telegramClient;
    private final UserStateService userStateService;

    @Override
    public String callbackData() {
        return Buttons.SET_TOTAL_KM.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        userStateService.removeState(callbackQuery.getMessage().getChatId());

        telegramClient.execute(EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(Buttons.SET_TOTAL_KM.getName())
                .replyMarkup(KeyboardFactory.probegMondaySet())
                .build());
    }
}
