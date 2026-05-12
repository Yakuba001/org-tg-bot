package com.orgtgbot.bot;

import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.bot.state.UserStateService;
import com.orgtgbot.bot.state.registry.StateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final StartCommand startCommand;
    private final CallbackRegistry callbackRegistry;
    private final StateRegistry stateRegistry;
    private final UserStateService userStateService;
    private final TelegramSender sender;

    public void dispatch(Update update) throws Exception {
        if (update.hasCallbackQuery()) {
            callbackRegistry.dispatch(update.getCallbackQuery());
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            processTextMessage(update);
        }
    }

    private void processTextMessage(Update update) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        UserState currentState = userStateService.getState(chatId);

        if (text.startsWith("/start")) {
            startCommand.execute(update);
            sender.deleteMessage(chatId, update.getMessage().getMessageId());
            return;
        }
        if (currentState != UserState.NONE) {
            stateRegistry.handle(currentState, update);
            return;
        }
        sender.deleteMessage(chatId, update.getMessage().getMessageId());
    }
}
