package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.bot.state.UserStateService;
import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class SetMondayMorningProbeg implements CallbackHandler {

    private final TelegramClient telegramClient;
    private final UserStateService userStateService;
    private final ProbegService probegService;

    @Override
    public String callbackData() {
        return Buttons.SET_MORNING_KM.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        userStateService.removeState(callbackQuery.getMessage().getChatId());
        Long chatId = callbackQuery.getMessage().getChatId();
        String mondayMorningKm = String.valueOf(probegService.getAll()
                .getFirst()
                .getMorningKm());

        telegramClient.execute(EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(Buttons.SET_MORNING_KM.getName() + ": " + mondayMorningKm + " km.")
                .replyMarkup(KeyboardFactory.probegMondaySet())
                .build());

        userStateService.setState(chatId, UserState.PROBEG_MONDAY_MORNING);
        userStateService.setMessageId(chatId, callbackQuery.getMessage().getMessageId());
    }
}
