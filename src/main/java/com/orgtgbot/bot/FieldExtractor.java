package com.orgtgbot.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.function.Function;

public class FieldExtractor {

    private static Message getMessageOrNull(Update update) {
        if (update == null) return null;
        if (update.hasMessage()) return update.getMessage();
        if (update.hasCallbackQuery()) return (Message) update.getCallbackQuery().getMessage();
        return null;
    }

    private static <R> R fieldExtractor(Update update, Function<Message, R> extractor) {
        Message message = getMessageOrNull(update);
        return message != null ? extractor.apply(message) : null;
    }

    public static Long extractChatId(Update update) {
        return fieldExtractor(update, Message::getChatId);
    }

    public static Integer extractMessageId(Update update) {
        return fieldExtractor(update, Message::getMessageId);
    }
}
