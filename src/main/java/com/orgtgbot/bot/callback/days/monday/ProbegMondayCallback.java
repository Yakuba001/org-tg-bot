package com.orgtgbot.bot.callback.days.monday;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class ProbegMondayCallback implements CallbackHandler {

    private final TelegramSender sender;

    @Override
    public Buttons callbackData() {
        return Buttons.PROBEG_MONDAY;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                Buttons.PROBEG_MONDAY.getName(),
                KeyboardFactory.probegMondayMenu()
        );
    }
}
