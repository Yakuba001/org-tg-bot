package com.orgtgbot.bot;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.bot.message.MessageType;
import com.orgtgbot.exception.exceptions.service.bot.telegram.sender.*;
import com.orgtgbot.service.services.message.MessageLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
public class TelegramSender {

    private final TelegramClient telegramClient;
    private final MessageLogService messageLogService;

    public void sendMessage(Long chatId, String text) {
        try {
            Message sentMessage = telegramClient.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
            messageLogService.log(chatId, sentMessage.getMessageId(), MessageType.BOT_TEXT);
        } catch (TelegramApiException e) {
            throw new TSSendMessageException(chatId, "Failed send message", e);
        }
    }

    public void sendDocument(Long chatId, byte[] bytes, String fileName) {
        try {
            Message sentMessage = telegramClient.execute(SendDocument.builder()
                    .chatId(chatId)
                    .document(new InputFile(new ByteArrayInputStream(bytes), fileName))
                    .build());
            messageLogService.log(chatId, sentMessage.getMessageId(), MessageType.BOT_TEXT);
        } catch (TelegramApiException e) {
            throw new TSSendDocumentException(chatId, "Failed send document", e);
        }
    }

    public void editMarkup(Long chatId, Integer messageId, String text, InlineKeyboardMarkup markup) {
        try {
            telegramClient.execute(EditMessageText.builder()
                    .chatId(chatId.toString())
                    .messageId(messageId)
                    .text(text)
                    .replyMarkup(markup)
                    .build());
        } catch (TelegramApiException e) {
            throw new TSEditMarkupException(chatId, "Failed edit message by messageId: " + messageId, e);
        }
    }

    public void deleteMessage(Long chatId, Integer messageId) {
        try {
            telegramClient.execute(DeleteMessage.builder()
                    .chatId(chatId.toString())
                    .messageId(messageId)
                    .build());
            messageLogService.deleteMessageByMessageId(chatId, messageId);
        } catch (TelegramApiException e) {
            throw new TSDeleteMessageException(chatId, "Failed delete message by messageId: " + messageId, e);
        }
    }

    public void answerCallback(String callbackQueryId) {
        try {
            telegramClient.execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQueryId)
                    .build());
        } catch (TelegramApiException e) {
            throw new TSAnswerCallbackException("Cant handle this callback: " + callbackQueryId, e);
        }
    }

    public void successUpdateText(Long chatId, Integer botMenuId, GeneralFields currentField, String text) {
        this.editMarkup(
                chatId,
                botMenuId,
                text,
                KeyboardFactory.buildMenuForGroup(currentField));
    }
}
