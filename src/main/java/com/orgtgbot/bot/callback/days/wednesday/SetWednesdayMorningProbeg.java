package com.orgtgbot.bot.callback.days.wednesday;

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
public class SetWednesdayMorningProbeg implements CallbackHandler {

    private final TelegramSender sender;
    private final ProbegService probegService;

    @Override
    public String callbackData() {
        return Buttons.SET_MORNING_WEDNESDAY_KM.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.SET_MORNING_WEDNESDAY_KM.getName() + " km.",
                KeyboardFactory.probegBack(Buttons.SET_MORNING_WEDNESDAY_KM)
        );
    }
}
