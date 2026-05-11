package com.orgtgbot.bot.state.registry;

import com.orgtgbot.bot.state.StateHandler;
import com.orgtgbot.bot.state.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StateRegistry {

    private final Map<UserState, StateHandler> handlers;

    public StateRegistry(List<StateHandler> all) {
        this.handlers = all.stream()
                .collect(Collectors.toUnmodifiableMap(
                        StateHandler::getSupportedHandle, h -> h));
    }

    public void handle(UserState state, Update update) {
        StateHandler handler = handlers.get(state);
        if (handler != null) {
            handler.handle(update);
        }
    }
}
