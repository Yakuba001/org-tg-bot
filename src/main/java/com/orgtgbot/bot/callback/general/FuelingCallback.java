package com.orgtgbot.bot.callback.general;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class FuelingCallback implements CallbackHandler {

    private final TelegramSender sender;
    private final GeneralService generalService;

    @Override
    public Buttons callbackData() {
        return Buttons.FUELING;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        String result = generalService.getAmount(callbackData());

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackData().getName() + ": " + result,
                KeyboardFactory.generalBack()
        );
    }
}
