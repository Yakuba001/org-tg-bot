package com.orgtgbot.bot.update;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.service.services.user.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@RequiredArgsConstructor
public class RegisteredTextUpdateHandler implements UpdateHandler {

    private final UserStateService userStateService;
    private final StartCommand startCommand;
    private final CallbackRegistry callbackRegistry;
    private final TelegramSender sender;

    @Override
    public boolean canHandle(Update update, boolean isUserRegistered) {
        return update.hasMessage() && update.getMessage().hasText() && isUserRegistered;
    }

    @Override
    public void handle(Update update, Long chatId) throws Exception {
        Message message = update.getMessage();
        Integer messageId = message.getMessageId();
        String text = message.getText().trim();
        GeneralFields currentField = userStateService.getState(chatId);

        sender.deleteMessage(chatId, messageId);

        if (text.startsWith("/start") || currentField == null || currentField == GeneralFields.NONE) {
            startCommand.execute(update);
        } else {
            callbackRegistry.handle(currentField, chatId, text, userStateService.getMessageId(chatId));
        }
    }
}
