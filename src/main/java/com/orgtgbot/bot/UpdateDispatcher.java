package com.orgtgbot.bot;

import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.config.TelegramBotProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
        GeneralFields field = userStateService.getState(chatId);
        Integer botMenuId = userStateService.getMessageId(chatId);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Integer messageId = update.getMessage().getMessageId();
            if (text.startsWith("/start")) {
                startCommand.execute(update);
                sender.deleteMessage(chatId, update.getMessage().getMessageId());
            } else {
                callbackRegistry.handle(field, chatId, text, botMenuId);
                sender.deleteMessage(chatId, messageId);
            }
            return;
        }
        if (field != GeneralFields.NONE && update.hasCallbackQuery()) {
            callbackRegistry.dispatch(
                    field,
                    update.getCallbackQuery(),
                    chatId,
                    update.getCallbackQuery().getMessage().getMessageId(),
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
