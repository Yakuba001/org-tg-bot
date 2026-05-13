package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class ProbegWednesdayCallback implements CallbackHandler {

    private final TelegramSender sender;

    @Override
    public String callbackData() {
        return Buttons.PROBEG_WEDNESDAY.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery, Buttons button) throws Exception {
        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.PROBEG_WEDNESDAY.getName(),
                KeyboardFactory.probegDayMenu()
        );
    }
}
