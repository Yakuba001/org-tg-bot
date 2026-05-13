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
public class SetMondayRoute implements CallbackHandler {

    private final TelegramSender sender;
    private final ProbegService probegService;

    @Override
    public String callbackData() {
        return Buttons.SET_ROUTE.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        String routeMonday = String.valueOf(probegService.getAll()
                .getFirst()
                .getRoute());

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.SET_ROUTE.getName() + ": " + routeMonday,
                KeyboardFactory.probegBack()
                );
    }
}
