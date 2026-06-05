package com.orgtgbot.bot.callback.days.friday;

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
public class SetFridayMorningProbeg implements CallbackHandler {

    private final TelegramSender sender;
    private final BotFacade botFacade;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.SET_MORNING_FRIDAY_KM;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        String result = botFacade.getAmount(callbackData());

        sender.editMarkup(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackData().getDescription()+ ": " + result + " км.",
                KeyboardFactory.dynamicBack(callbackData())
        );
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        if (!text.matches("\\d+")) {
            sender.editMarkup(chatId, botMenuId, "Ошибка! Введите число.",
                    KeyboardFactory.buildMenuForGroup(callbackData()));
        } else {
            botFacade.setAmount(callbackData(), text.trim());
            sender.editMarkup(chatId, botMenuId, "Данные приняты!\n",
                    KeyboardFactory.buildMenuForGroup(callbackData()));
        }
    }
}
