package com.orgtgbot.bot.state.registry;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.state.StateHandler;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.bot.state.UserStateService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StateRegistry {

    private final Map<UserState, StateHandler> handlers;
    private final UserStateService userStateService;
    private final TelegramSender sender;

    public StateRegistry(List<StateHandler> all, UserStateService userStateService, TelegramSender sender) {
        this.handlers = all.stream()
                .collect(Collectors.toUnmodifiableMap(
                        StateHandler::getSupportedHandle, h -> h));
        this.userStateService = userStateService;
        this.sender = sender;
    }

    public void handle(UserState state, Update update) {
        StateHandler handler = handlers.get(state);
        if (handler != null) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            Integer botMenuId = userStateService.getMessageId(chatId);

            handler.handle(chatId, text, botMenuId, sender);

            userStateService.removeState(chatId);
            sender.deleteMessage(chatId, update.getMessage().getMessageId());
        }
    }
}
