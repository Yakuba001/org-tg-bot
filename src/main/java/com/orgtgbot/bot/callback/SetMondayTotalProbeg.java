package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SetMondayTotalProbeg implements CallbackHandler {

    private final TelegramSender sender;
    private final ProbegService probegService;

    @Override
    public String callbackData() {
        return Buttons.SET_TOTAL_KM.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery, Buttons button) throws Exception {
        String mondayTotalKm = String.valueOf(probegService.getAll()
                .getFirst()
                .getTotalKm());

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.SET_TOTAL_KM.getName() + ": " + mondayTotalKm + " km.",
                KeyboardFactory.probegBack(button)
        );
    }
}
