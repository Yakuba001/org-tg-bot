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
public class InputDate implements StateHandler {

    private final GeneralService generalService;

    @Override
    public UserState getSupportedHandle() {
        return UserState.DATE;
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        generalService.setAmount(getSupportedHandle(), text.trim());
        sender.editMarkup(chatId, botMenuId, "Данные приняты!\n", KeyboardFactory.probegMondayMenu());
    }
}
