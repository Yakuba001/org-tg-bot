package com.orgtgbot.bot.callback.days.monday;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SetMondayEveningProbeg implements CallbackHandler {

    private final TelegramSender sender;
    private final ProbegService probegService;

    @Override
    public String callbackData() {
        return Buttons.SET_EVENING_MONDAY_KM.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        String mondayEveningKm = String.valueOf(probegService.getAll()
                .getFirst()
                .getEveningKm());

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.SET_EVENING_MONDAY_KM.getName() + ": " + mondayEveningKm + " km.",
                KeyboardFactory.probegBack(Buttons.SET_EVENING_MONDAY_KM)
        );
    }
}
