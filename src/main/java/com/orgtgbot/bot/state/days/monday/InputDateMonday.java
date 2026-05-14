package com.orgtgbot.bot.state.days.monday;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.state.StateHandler;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.service.DateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InputDateMonday implements StateHandler {

    private final DateService dateService;

    @Override
    public UserState getSupportedHandle() {
        return UserState.DATE_MONDAY;
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        dateService.setDate(getSupportedHandle(), text.trim());
        sender.editMarkup(chatId, botMenuId, "Данные приняты!\n", KeyboardFactory.probegMondayMenu());
    }
}
