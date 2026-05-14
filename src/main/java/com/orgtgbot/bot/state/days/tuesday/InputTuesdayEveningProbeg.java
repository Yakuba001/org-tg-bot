package com.orgtgbot.bot.state.days.tuesday;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.state.StateHandler;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InputTuesdayEveningProbeg implements StateHandler {

    private final ProbegService probegService;

    @Override
    public UserState getSupportedHandle() {
        return UserState.PROBEG_EVENING_TUESDAY;
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        if (!text.matches("\\d+")) {
            sender.editMarkup(chatId, botMenuId, "Ошибка! Введите число.", KeyboardFactory.probegTuesdayMenu());
        } else {
//            String report = probegService.changeMonday(List.of(Integer.parseInt(text.trim())));
            sender.editMarkup(chatId, botMenuId, "Данные приняты!\n", KeyboardFactory.probegTuesdayMenu());
        }
    }
}
