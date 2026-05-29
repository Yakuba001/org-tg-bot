package com.orgtgbot.bot.callback.days.tuesday;

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
public class SetTuesdayEveningProbeg implements CallbackHandler {

    private final TelegramSender sender;
    private final ProbegService probegService;

    @Override
    public Buttons callbackData() {
        return Buttons.SET_EVENING_TUESDAY_KM;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        String result = probegService.getAmounts(callbackData());

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.SET_EVENING_TUESDAY_KM.getName() + ": " + result + " км.",
                KeyboardFactory.probegBack(Buttons.SET_EVENING_TUESDAY_KM)
        );
    }
}
