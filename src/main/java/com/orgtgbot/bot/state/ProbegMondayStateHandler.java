package com.orgtgbot.bot.state;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProbegMondayStateHandler implements StateHandler {

    private final UserStateService userStateService;
    private final ProbegService probegService;
    private final TelegramSender sender;

    @Override
    public UserState getSupportedHandle() {
        return UserState.PROBEG_MONDAY;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Integer botMenuId = userStateService.getMessageId(chatId); // 008

        if (!text.matches("\\d+")) {
            sender.editMarkup(chatId, botMenuId, "Ошибка! Введите число.", KeyboardFactory.probegMenu());
        } else {
            String report = probegService.changeMonday(List.of(Integer.parseInt(text.trim())));
            sender.editMarkup(chatId, botMenuId, "Данные приняты!\n" + report, KeyboardFactory.probegMenu());
        }
        userStateService.removeState(chatId);
        sender.deleteMessage(chatId, update.getMessage().getMessageId());
    }
}
