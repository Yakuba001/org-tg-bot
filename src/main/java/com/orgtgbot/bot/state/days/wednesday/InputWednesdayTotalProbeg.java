package com.orgtgbot.bot.state.days.wednesday;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.state.StateHandler;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InputWednesdayTotalProbeg implements StateHandler {

    private final ProbegService probegService;

    @Override
    public UserState getSupportedHandle() {
        return UserState.PROBEG_TOTAL_WEDNESDAY;
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        if (!text.matches("\\d+")) {
            sender.editMarkup(chatId, botMenuId, "Ошибка! Введите число.", KeyboardFactory.probegWednesdayMenu());
        } else {
            probegService.setAmounts(getSupportedHandle(), Integer.parseInt(text.trim()), null);
            sender.editMarkup(chatId, botMenuId, "Данные приняты!\n", KeyboardFactory.probegWednesdayMenu());
        }
    }
}
