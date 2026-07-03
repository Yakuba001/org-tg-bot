package com.orgtgbot.bot.update;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CallbackUpdateHandler implements UpdateHandler {

    private final CallbackRegistry callbackRegistry;

    @Override
    public boolean canHandle(Update update, boolean isUserRegistered) {
        return update.hasCallbackQuery() && isUserRegistered;
    }

    @Override
    public void handle(Update update, Long chatId) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        GeneralFields clickedField = GeneralFields.valueOf(callbackQuery.getData());
        Integer callbackMessageId = callbackQuery.getMessage().getMessageId();
        callbackRegistry.dispatch(clickedField, callbackQuery, chatId, callbackMessageId);
    }
}
