package com.orgtgbot.bot;

import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.command.registry.CommandRegistry;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.bot.state.UserStateService;
import com.orgtgbot.bot.state.registry.StateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final CommandRegistry commandRegistry;
    private final CallbackRegistry callbackRegistry;
    private final StateRegistry stateRegistry;
    private final UserStateService userStateService;

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
        if (text.startsWith("/")) {
            commandRegistry.resolve(text);
        }
    }
}
