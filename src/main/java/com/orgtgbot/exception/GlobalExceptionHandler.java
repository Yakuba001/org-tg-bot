package com.orgtgbot.exception;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.exception.exceptions.BotException;
import com.orgtgbot.exception.exceptions.callback.DispatcherHandleException;
import com.orgtgbot.exception.exceptions.service.DateIndexOutOfBoundException;
import com.orgtgbot.exception.exceptions.service.DayNotFoundException;
import com.orgtgbot.exception.exceptions.service.ExcelGeneratorException;
import com.orgtgbot.exception.exceptions.service.WorkspaceNotFoundException;
import com.orgtgbot.exception.exceptions.service.bot.telegram.client.TelegramClientExecutorException;
import com.orgtgbot.exception.exceptions.service.bot.telegram.sender.*;
import com.orgtgbot.exception.exceptions.service.gemini.DeserializeGeminiResponse;
import com.orgtgbot.exception.exceptions.service.gemini.GeminiParseTextException;
import com.orgtgbot.exception.exceptions.service.gemini.GeminiParseVoiceException;
import com.orgtgbot.exception.exceptions.service.http.SendHttpRequestToGeminiException;
import com.orgtgbot.exception.exceptions.service.reminder.SendRemindException;
import com.orgtgbot.exception.exceptions.service.voice.DownloadFileFromTelegramException;
import com.orgtgbot.exception.exceptions.service.voice.FailedHandleVoiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final TelegramSender sender;

    private static final Map<Class<? extends Exception>, String> ERROR_MESSAGES = Map.ofEntries(
            Map.entry(GeminiParseTextException.class, "Fail, try create remind again."),
            Map.entry(GeminiParseVoiceException.class, "Fail, try create remind by text."),
            Map.entry(SendHttpRequestToGeminiException.class, "Fail, from AI side."),
            Map.entry(DeserializeGeminiResponse.class, "Fail, from AI side."),
            Map.entry(SendRemindException.class, "Send remind failed."),
            Map.entry(DownloadFileFromTelegramException.class, "Fail, try create remind by text."),
            Map.entry(FailedHandleVoiceException.class, "Fail, try create remind by text."),
            Map.entry(WorkspaceNotFoundException.class, "Try again later."),
            Map.entry(DayNotFoundException.class, "Add this day and try again."),
            Map.entry(ExcelGeneratorException.class, "Fail, create excel file."),
            Map.entry(DateIndexOutOfBoundException.class, "You input wrong day, try again."),
            Map.entry(TSAnswerCallbackException.class, "Something went wrong."),
            Map.entry(TSSendDocumentException.class, "Something went wrong."),
            Map.entry(TSDeleteMessageException.class, "Something went wrong."),
            Map.entry(TSEditMarkupException.class, "Something went wrong."),
            Map.entry(TSSendMessageException.class, "Something went wrong."),
            Map.entry(DispatcherHandleException.class, "Fail in dispatcher."),
            Map.entry(TelegramClientExecutorException.class, "Something went wrong.")
    );

    public void handle(Exception e, Long chatId, Integer messageId) {
        Long resolvedChatId = (e instanceof BotException be && be.getChatId() != null)
                ? be.getChatId()
                : chatId;
        log.error("[BOT-ERROR] Exception caught in GEH: {}", e.getMessage(), e);
        String errorUserMessage = ERROR_MESSAGES.getOrDefault(
                e.getClass(),
                "Something went wrong. Please try again later."
        );
        safeEdit(resolvedChatId, errorUserMessage, messageId);
    }

    private void safeEdit(Long chatId, String text, Integer messageId) {
        if (chatId == null) return;
        try {
            sender.editMarkup(chatId, messageId, text, KeyboardFactory.buildMenuForGroup(GeneralFields.MAIN_MENU));
        } catch (Exception e) {
            log.error("Failed to send error message to chatId={}: ", chatId, e);
        }
    }
}
