package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class ProbegFridayCallback implements CallbackHandler {

    private final TelegramSender sender;

    @Override
    public String callbackData() {
        return Buttons.PROBEG_FRIDAY.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.PROBEG_FRIDAY.getName(),
                KeyboardFactory.probegDayMenu()
        );
    }
}
