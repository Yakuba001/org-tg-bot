package com.orgtgbot.bot.callback.registry;

import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.bot.state.UserStateService;
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
    private final UserStateService userStateService;

    public CallbackRegistry(List<CallbackHandler> all,
                            TelegramClient telegramClient,
                            UserStateService userStateService) {
        this.telegramClient = telegramClient;
        this.handlers = all.stream()
                .collect(Collectors.toUnmodifiableMap(
                        CallbackHandler::callbackData, h -> h));
        this.userStateService = userStateService;
    }

    public void dispatch(CallbackQuery callbackQuery) throws Exception {
        userStateService.removeState(callbackQuery.getMessage().getChatId());
        answerCallback(callbackQuery.getId());
        Long chatId = callbackQuery.getMessage().getChatId();
        Buttons data = Buttons.valueOf(callbackQuery.getData());
        CallbackHandler handler = handlers.get(data.name());

        if (handler == null) {
            log.warn("Неизвестный callback: {}", data.name());
            return;
        }

        handler.handle(callbackQuery);

        userStateService.setState(chatId, stateSwitcher(data));
        userStateService.setMessageId(chatId, callbackQuery.getMessage().getMessageId());
    }

    private void answerCallback(String callbackQueryId) throws TelegramApiException {
        telegramClient.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .build());
    }

    private UserState stateSwitcher(Buttons button) {
        switch (button) {
            case SET_MORNING_KM -> {
                return UserState.PROBEG_MORNING;
            }
            case SET_EVENING_KM -> {
                return UserState.PROBEG_EVENING;
            }
            case SET_TOTAL_KM -> {
                return UserState.PROBEG_TOTAL;
            }
            case SET_ROUTE -> {
                return UserState.ROUTE_MONDAY;
            }
            default -> {
                return UserState.NONE;
            }
        }
    }
}
