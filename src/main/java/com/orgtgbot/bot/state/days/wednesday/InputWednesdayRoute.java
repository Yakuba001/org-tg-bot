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
public class InputWednesdayRoute implements StateHandler {

    private final ProbegService probegService;

    @Override
    public UserState getSupportedHandle() {
        return UserState.ROUTE_WEDNESDAY;
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        probegService.setAmounts(getSupportedHandle(), null, text.trim());
        sender.editMarkup(chatId, botMenuId, "Данные приняты!\n", KeyboardFactory.probegWednesdayMenu());
    }
}
