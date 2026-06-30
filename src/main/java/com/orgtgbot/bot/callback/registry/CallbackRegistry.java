package com.orgtgbot.bot.callback.registry;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.interactions.CallbackHandler;
import com.orgtgbot.bot.callback.registry.core.interactions.ClickableHandler;
import com.orgtgbot.bot.callback.registry.core.interactions.TextAnswerableHandler;
import com.orgtgbot.service.services.user.UserStateService;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CallbackRegistry {

    private final Map<GeneralFields, CallbackHandler> handlers;
    private final TelegramSender telegramSender;
    private final UserStateService userStateService;

    public CallbackRegistry(List<CallbackHandler> all,
                            TelegramSender telegramSender,
                            UserStateService userStateService) {
        this.telegramSender = telegramSender;
        this.handlers = all.stream()
                .collect(Collectors.toUnmodifiableMap(
                        CallbackHandler::callbackData, h -> h));
        this.userStateService = userStateService;
    }

    public void dispatch(GeneralFields field,
                         CallbackQuery callbackQuery,
                         Long chatId,
                         Integer callbackMessageId,
                         Integer botMenuId) throws Exception {
        CallbackHandler handler = handlers.get(field);
        telegramSender.answerCallback(callbackQuery.getId());
        if (handler != null) {
            if (handler instanceof ClickableHandler clickableHandler) {
                clickableHandler.handle(callbackQuery);
                userStateService.setStateAndMessageId(chatId, field, callbackMessageId);
            } else {
                log.warn("Найден хэндлер для {}, но он не реализует ClickableHandler!", field);
                sendDispatcherError(chatId, botMenuId);
            }
        } else {
            sendDispatcherError(chatId, botMenuId);
        }
    }

    public void handle(GeneralFields field,
                       Long chatId,
                       String text,
                       Integer botMenuId) throws Exception {
        CallbackHandler handler = handlers.get(field);
        if (handler != null) {
            if (handler instanceof TextAnswerableHandler answerableHandler) {
                answerableHandler.handle(chatId, text, botMenuId, telegramSender);
                userStateService.clearState(chatId);
            } else {
                log.warn("Хэндлер для стейта {} найден, но он не ожидает текстового ввода (нет TextAnswerableHandler)", field);
                userStateService.clearState(chatId);
            }
        } else {
            log.warn("Неожиданный стейт: {}", field);
        }
    }

    private void sendDispatcherError(Long chatId, Integer botMenuId) {
        telegramSender.editMarkup(chatId, botMenuId, "Ошибка диспетчера!\n",
                KeyboardFactory.buildMenuForGroup(GeneralFields.MAIN_MENU));
        userStateService.clearState(chatId);
    }
}
