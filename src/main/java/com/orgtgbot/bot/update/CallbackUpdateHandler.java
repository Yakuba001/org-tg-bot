package com.orgtgbot.bot.update;

import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.service.services.user.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CallbackUpdateHandler implements UpdateHandler {

    private final CallbackRegistry callbackRegistry;
    private final UserStateService userStateService;

    @Override
    public boolean canHandle(Update update, boolean isUserRegistered) {
        return update.hasCallbackQuery() && isUserRegistered;
    }

    @Override
    public void handle(Update update, Long chatId) throws Exception {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        GeneralFields clickedField = GeneralFields.valueOf(callbackQuery.getData());
        Integer callbackMessageId = callbackQuery.getMessage().getMessageId();
        Integer botMenuId = userStateService.getMessageId(chatId);
        callbackRegistry.dispatch(clickedField, callbackQuery, chatId, callbackMessageId, botMenuId);
    }
}
