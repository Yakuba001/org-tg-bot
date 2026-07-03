package com.orgtgbot.bot.callback.registry;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.interactions.CallbackHandler;
import com.orgtgbot.bot.callback.registry.core.interactions.ClickableHandler;
import com.orgtgbot.bot.callback.registry.core.interactions.TextAnswerableHandler;
import com.orgtgbot.exception.exceptions.callback.DispatcherHandleException;
import com.orgtgbot.service.services.user.UserStateService;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CallbackRegistry {

    private final Map<GeneralFields, CallbackHandler> handlers;
    private final TelegramSender telegramSender;
    private final UserStateService userStateService;

    public CallbackRegistry(List<CallbackHandler> all,
                            TelegramSender telegramSender,
                            UserStateService userStateService) {
        this.handlers = all.stream()
                .collect(Collectors.toUnmodifiableMap(
                        CallbackHandler::callbackData, Function.identity()));
        this.telegramSender = telegramSender;
        this.userStateService = userStateService;
    }

    public void dispatch(GeneralFields field,
                         CallbackQuery callbackQuery,
                         Long chatId,
                         Integer callbackMessageId) {
        CallbackHandler handler = handlers.get(field);
        telegramSender.answerCallback(callbackQuery.getId());
        if (handler instanceof ClickableHandler clickableHandler) {
            clickableHandler.handle(callbackQuery);
            userStateService.setStateAndMessageId(chatId, field, callbackMessageId);
            return;
        }
        throw new DispatcherHandleException(chatId, "You can`t handle this handler(dispatch): " + handler);
    }

    public void handle(GeneralFields field,
                       Long chatId,
                       String text,
                       Integer botMenuId) {
        CallbackHandler handler = handlers.get(field);
        if (handler instanceof TextAnswerableHandler answerableHandler) {
            answerableHandler.handle(chatId, text, botMenuId, telegramSender);
            userStateService.clearState(chatId);
            return;
        }
        throw new DispatcherHandleException(chatId, "You can`t handle this handler(handle): " + handler);
    }
}
