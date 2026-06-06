package com.orgtgbot.bot.callback.days.thursday;

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
public class SetThursdayTotalProbeg implements CallbackHandler {

    private final TelegramSender sender;
    private final BotFacade botFacade;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.SET_TOTAL_THURSDAY_KM;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        Long chatId = callbackQuery.getMessage().getChatId();
        String result = botFacade.getAmount(callbackData(), chatId);

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackData().getDescription() + ": " + result + " км.",
                KeyboardFactory.dynamicBack(callbackData())
        );
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        if (!text.matches("\\d+")) {
            sender.editMarkup(chatId, botMenuId, "Ошибка! Введите число.",
                    KeyboardFactory.buildMenuForGroup(callbackData()));
        } else {
            botFacade.setAmount(callbackData(), text.trim(), chatId);
            sender.editMarkup(chatId, botMenuId, "Данные приняты!\n",
                    KeyboardFactory.buildMenuForGroup(callbackData()));
        }
    }
}
