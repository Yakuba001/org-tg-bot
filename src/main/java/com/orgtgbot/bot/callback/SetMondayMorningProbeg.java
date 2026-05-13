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
public class SetMondayMorningProbeg implements CallbackHandler {

    private final TelegramSender sender;
    private final ProbegService probegService;

    @Override
    public String callbackData() {
        return Buttons.SET_MORNING_KM.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery, Buttons button) throws Exception {
        String mondayMorningKm = String.valueOf(probegService.getAll()
                .getFirst()
                .getMorningKm());

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.SET_MORNING_KM.getName() + ": " + mondayMorningKm + " km.",
                KeyboardFactory.probegBack(button)
        );
    }
}
