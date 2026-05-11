package com.orgtgbot.bot;

import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.bot.state.UserStateService;
import com.orgtgbot.bot.state.registry.StateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final StartCommand startCommand;
    private final CallbackRegistry callbackRegistry;
    private final StateRegistry stateRegistry;
    private final UserStateService userStateService;
    private final TelegramSender sender;

    public void dispatch(Update update) {
        if (update.hasCallbackQuery()) {
            callbackRegistry.dispatch(update.getCallbackQuery());
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            processTextMessage(update);
        }
    }

    private void processTextMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        UserState currentState = userStateService.getState(chatId);

        if (currentState != UserState.NONE && currentState != null) {
            stateRegistry.handle(currentState, update);
            return;
        }
        if (text.startsWith("/start")) {
            startCommand.execute(update);
            sender.deleteMessage(chatId, update.getMessage().getMessageId());
        } else {
            sender.deleteMessage(chatId, update.getMessage().getMessageId());
        }
    }
}
