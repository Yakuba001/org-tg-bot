package com.orgtgbot.bot.state.general;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.state.StateHandler;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.service.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InputFuelNorm implements StateHandler {

    private final GeneralService generalService;

    @Override
    public UserState getSupportedHandle() {
        return UserState.FUEL_NORM;
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        if (!text.matches("-?\\d+([.,]\\d+)?")) {
            sender.editMarkup(chatId, botMenuId, "Ошибка! Введите число с плавающей точкой.",
                    KeyboardFactory.generalMenu());
        } else {
            generalService.setAmount(getSupportedHandle(), text.trim());
            sender.editMarkup(chatId, botMenuId, "Данные приняты!\n", KeyboardFactory.generalMenu());
        }
    }
}
