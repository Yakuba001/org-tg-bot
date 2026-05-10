package com.orgtgbot.bot;

import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.callback.state.UserStateService;
import com.orgtgbot.bot.command.CommandHandler;
import com.orgtgbot.bot.command.registry.CommandRegistry;
import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
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
            Long chatId = update.getMessage().getChatId();
            log.debug("chat={} text={}", chatId, text);

            String state = userStateService.getState(chatId);
            if (!state.isEmpty()) {
                handleStatefulUpdate(chatId, text, state);
                return;
            }

            CommandHandler handler = registry.resolve(text);
            String response = handler.execute(update);
            if (response != null && !response.isBlank()) {
                sender.sendText(chatId, response);
            }
        }
        if (update.hasCallbackQuery()) {
            log.debug("callback={}", update.getCallbackQuery().getData());
            callbackRegistry.dispatch(update.getCallbackQuery());
        }
    }

    private void handleStatefulUpdate(Long chatId, String text, String state) {
        if ("WAITING_PROBEG_MONDAY".equals(state)) {
            try {
                int km = Integer.parseInt(text.trim());
                String report = probegService.changeMonday(List.of(km));
                sender.sendText(chatId, "Данные приняты!\n" + report);
                userStateService.removeState(chatId);
            } catch (NumberFormatException e) {
                sender.sendText(chatId, "Пожалуйста, введите пробег целым числом (например, 12).");
            }
        }
    }
}
