package com.orgtgbot.bot.state.days.monday;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.state.StateHandler;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.bot.state.UserStateService;
import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InputMondayMorningProbeg implements StateHandler {

    private final UserStateService userStateService;
    private final ProbegService probegService;
    private final TelegramSender sender;

    @Override
    public UserState getSupportedHandle() {
        return UserState.PROBEG_MORNING_MONDAY;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Integer botMenuId = userStateService.getMessageId(chatId);

        if (!text.matches("\\d+")) {
            sender.editMarkup(chatId, botMenuId, "Ошибка! Введите число.", KeyboardFactory.probegMondayMenu());
        } else {
//            String report = probegService.changeMonday(List.of(Integer.parseInt(text.trim())));
            sender.editMarkup(chatId, botMenuId, "Данные приняты!\n", KeyboardFactory.probegMondayMenu());
        }
        userStateService.removeState(chatId);
        sender.deleteMessage(chatId, update.getMessage().getMessageId());
    }
}
