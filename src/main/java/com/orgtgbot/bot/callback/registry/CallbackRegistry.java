package com.orgtgbot.bot.callback.registry;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.callback.GeneralFields;
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
            handler.handle(callbackQuery);
            userStateService.setState(chatId, field);
            userStateService.setMessageId(chatId, callbackMessageId);
        } else {
            telegramSender.editMarkup(chatId, botMenuId, "Ошибка диспетчера!\n", KeyboardFactory.generalMenu());
            userStateService.removeState(chatId);
        }
    }

    public void handle(GeneralFields field,
                       Long chatId,
                       String text,
                       Integer botMenuId) throws Exception {
        CallbackHandler handler = handlers.get(field);
        if (handler != null) {
            handler.handle(chatId, text, botMenuId, telegramSender);
            userStateService.removeState(chatId);
        } else {
            log.warn("Неожиданный стейт: {}", field);
        }
    }
}
