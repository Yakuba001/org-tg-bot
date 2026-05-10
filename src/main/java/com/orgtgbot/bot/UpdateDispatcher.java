package com.orgtgbot.bot;

import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.callback.state.UserStateService;
import com.orgtgbot.bot.command.CommandHandler;
import com.orgtgbot.bot.command.registry.CommandRegistry;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final CommandRegistry registry;
    private final CallbackRegistry callbackRegistry;
    private final TelegramSender sender;

    private final UserStateService userStateService;
    private final ProbegService probegService;

    public void dispatch(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            String firstPart = text.trim().split("\\s+", 2)[0];
            Long chatId = update.getMessage().getChatId();
            String state = userStateService.getState(chatId);

            if (!state.isEmpty()) {
                if (!firstPart.matches("\\d+")) {
                    sender.sendText(
                            chatId,
                            "Пожалуйста, введите пробег целым числом (например, 12).",
                            KeyboardFactory.probegMenu());
                    userStateService.removeState(chatId);
                    return;
                }
                handleStatefulUpdate(chatId, firstPart, state);
                return;
            }

            CommandHandler handler = registry.resolve(text);
            String response = handler.execute(update);
            if (response != null && !response.isBlank()) {
                sender.sendText(chatId, response, KeyboardFactory.mainMenu());
            }
        }
        if (update.hasCallbackQuery()) {
            callbackRegistry.dispatch(update.getCallbackQuery());
        }
    }

    private void handleStatefulUpdate(Long chatId, String firstPart, String state) {
        if ("WAITING_PROBEG_MONDAY".equals(state)) {
            String report = probegService.changeMonday(List.of(Integer.parseInt(firstPart.trim())));
            sender.sendText(chatId, "Данные приняты!\n" + report, KeyboardFactory.probegMenu());
            userStateService.removeState(chatId);
        }
    }
}
