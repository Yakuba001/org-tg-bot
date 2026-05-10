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
            Long chatId = update.getMessage().getChatId();
            Integer messageId = update.getMessage().getMessageId();
            String state = userStateService.getState(chatId);
            if (text.equals("/start")) {
                sender.deleteMessage(chatId, update.getMessage().getMessageId());
                sender.sendText(chatId, "Добро пожаловать в Органайзер!", KeyboardFactory.mainMenu());
                return;
            }



            if (!state.isEmpty()) {
                Integer botMenuId = userStateService.getMessageId(chatId);
                if (!text.matches("\\d+")) {
                    sender.editMarkup(
                            chatId,
                            botMenuId,
                            "Пожалуйста, введите целое число (например, 12).",
                            KeyboardFactory.probegMenu());
                    userStateService.removeState(chatId);
                    sender.deleteMessage(chatId, update.getMessage().getMessageId());
                    return;
                }
                handleStatefulUpdate(chatId, text, state);
                userStateService.removeState(chatId);
                sender.deleteMessage(chatId, update.getMessage().getMessageId());
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

    private void handleStatefulUpdate(Long chatId, String text, String state) {
        if ("WAITING_PROBEG_MONDAY".equals(state)) {
            String report = probegService.changeMonday(List.of(Integer.parseInt(text.trim())));
            Integer messageId = userStateService.getMessageId(chatId);
            sender.editMarkup(chatId, messageId, "Данные приняты!\n" + report, KeyboardFactory.probegMenu());
        }
    }
}
