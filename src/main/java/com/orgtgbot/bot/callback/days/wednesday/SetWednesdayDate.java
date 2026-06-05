package com.orgtgbot.bot.callback.days.wednesday;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.BotFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SetWednesdayDate implements CallbackHandler {

    private final TelegramSender sender;
    private final BotFacade botFacade;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.WEDNESDAY_DATE;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        String result = botFacade.getAmount(callbackData());
        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackData().getDescription() + ": " + result,
                KeyboardFactory.dynamicBack(callbackData())
        );
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        botFacade.setAmount(callbackData(), text.trim());
        sender.editMarkup(chatId, botMenuId, "Данные приняты!\n",
                KeyboardFactory.buildMenuForGroup(callbackData()));
    }
}
