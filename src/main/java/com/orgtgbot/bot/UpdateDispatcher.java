package com.orgtgbot.bot;

import com.orgtgbot.bot.callback.UserStateService;
import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.config.TelegramBotProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final StartCommand startCommand;
    private final CallbackRegistry callbackRegistry;
    private final UserStateService userStateService;
    private final TelegramSender sender;
    private final TelegramBotProperties properties;

    public void dispatch(Update update) throws Exception {
        Long chatId = extractChatId(update);
        if (chatId == null) return;
        if (!properties.allowedUsers().contains(chatId)) return;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Integer messageId = update.getMessage().getMessageId();
            if (text.startsWith("/start")) {
                startCommand.execute(update);
                userStateService.clearState(chatId);
                sender.deleteMessage(chatId, update.getMessage().getMessageId());
            } else {
                GeneralFields field = userStateService.getState(chatId);
                callbackRegistry.handle(field, chatId, text, userStateService.getMessageId(chatId));
                sender.deleteMessage(chatId, messageId);
            }
            return;
        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            GeneralFields clickedField = GeneralFields.valueOf(callbackQuery.getData());
            Integer callbackMessageId = callbackQuery.getMessage().getMessageId();
            Integer botMenuId = userStateService.getMessageId(chatId);
            callbackRegistry.dispatch(
                    clickedField,
                    callbackQuery,
                    chatId,
                    callbackMessageId,
                    botMenuId);
        }
    }

    private Long extractChatId(Update update) {
        if (update.hasMessage())
            return update.getMessage().getChatId();
        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null)
            return update.getCallbackQuery().getMessage().getChatId();
        return null;
    }
}
