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

    private final Map<Buttons, CallbackHandler> handlers;
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
        CallbackHandler handler = handlers.get(data);

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
            case SET_MORNING_MONDAY_KM -> {
                return UserState.PROBEG_MORNING_MONDAY;
            }
            case SET_EVENING_MONDAY_KM -> {
                return UserState.PROBEG_EVENING_MONDAY;
            }
            case SET_TOTAL_MONDAY_KM -> {
                return UserState.PROBEG_TOTAL_MONDAY;
            }
            case SET_MONDAY_ROUTE -> {
                return UserState.ROUTE_MONDAY;
            }
            case SET_MORNING_TUESDAY_KM -> {
                return UserState.PROBEG_MORNING_TUESDAY;
            }
            case SET_EVENING_TUESDAY_KM -> {
                return UserState.PROBEG_EVENING_TUESDAY;
            }
            case SET_TOTAL_TUESDAY_KM -> {
                return UserState.PROBEG_TOTAL_TUESDAY;
            }
            case SET_TUESDAY_ROUTE -> {
                return UserState.ROUTE_TUESDAY;
            }
            case SET_MORNING_WEDNESDAY_KM -> {
                return UserState.PROBEG_MORNING_WEDNESDAY;
            }
            case SET_EVENING_WEDNESDAY_KM -> {
                return UserState.PROBEG_EVENING_WEDNESDAY;
            }
            case SET_TOTAL_WEDNESDAY_KM -> {
                return UserState.PROBEG_TOTAL_WEDNESDAY;
            }
            case SET_WEDNESDAY_ROUTE -> {
                return UserState.ROUTE_WEDNESDAY;
            }
            case SET_MORNING_THURSDAY_KM -> {
                return UserState.PROBEG_MORNING_THURSDAY;
            }
            case SET_EVENING_THURSDAY_KM -> {
                return UserState.PROBEG_EVENING_THURSDAY;
            }
            case SET_TOTAL_THURSDAY_KM -> {
                return UserState.PROBEG_TOTAL_THURSDAY;
            }
            case SET_THURSDAY_ROUTE -> {
                return UserState.ROUTE_THURSDAY;
            }
            case SET_MORNING_FRIDAY_KM -> {
                return UserState.PROBEG_MORNING_FRIDAY;
            }
            case SET_EVENING_FRIDAY_KM -> {
                return UserState.PROBEG_EVENING_FRIDAY;
            }
            case SET_TOTAL_FRIDAY_KM -> {
                return UserState.PROBEG_TOTAL_FRIDAY;
            }
            case SET_FRIDAY_ROUTE -> {
                return UserState.ROUTE_FRIDAY;
            }
            default -> {
                return UserState.NONE;
            }
        }
    }
}
