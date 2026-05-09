package com.orgtgbot.bot.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CallbackRegistry {

    private final Map<String, CallbackHandler> handlers;

    public CallbackRegistry(List<CallbackHandler> all) {
        this.handlers = all.stream()
                .collect(Collectors.toUnmodifiableMap(
                        CallbackHandler::callbackData, h -> h));
    }

    public void dispatch(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        CallbackHandler handler = handlers.get(data);

        if (handler == null) {
            log.warn("Неизвестный callback: {}", data);
            return;
        }
        handler.handle(callbackQuery);
    }
}
