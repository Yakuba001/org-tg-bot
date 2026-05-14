package com.orgtgbot.bot.callback.days.wednesday;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.DateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SetWednesdayDate implements CallbackHandler {

    private final TelegramSender sender;
    private final DateService dateService;

    @Override
    public Buttons callbackData() {
        return Buttons.WEDNESDAY_DATE;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        String result = dateService.getDate(callbackData());
        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackData().getName() + ": " + result,
                KeyboardFactory.probegBack(callbackData())
        );
    }
}
