package com.orgtgbot.bot.callback.registry;

import com.orgtgbot.bot.callback.CallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CallbackRegistry {

    private final Map<String, CallbackHandler> handlers;
    private final TelegramClient telegramClient;

    public CallbackRegistry(List<CallbackHandler> all, TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
        this.handlers = all.stream()
                .collect(Collectors.toUnmodifiableMap(
                        CallbackHandler::callbackData, h -> h));
    }

    public void dispatch(CallbackQuery callbackQuery) throws Exception {
        answerCallback(callbackQuery.getId());

        String data = callbackQuery.getData();
        CallbackHandler handler = handlers.get(data);

        if (handler == null) {
            log.warn("Неизвестный callback: {}", data);
            return;
        }
        handler.handle(callbackQuery);
    }

    private void answerCallback(String callbackQueryId) throws TelegramApiException {
        telegramClient.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .build());
    }
}
