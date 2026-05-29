package com.orgtgbot.bot.callback.days.thursday;

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
public class SetThursdayRoute implements CallbackHandler {

    private final TelegramSender sender;
    private final ProbegService probegService;

    @Override
    public Buttons callbackData() {
        return Buttons.SET_THURSDAY_ROUTE;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        String result = probegService.getAmounts(callbackData());

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.SET_THURSDAY_ROUTE.getName() + ": " + result,
                KeyboardFactory.probegBack(Buttons.SET_THURSDAY_ROUTE)
        );
    }
}
