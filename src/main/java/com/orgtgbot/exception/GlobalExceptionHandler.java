package com.orgtgbot.exception;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.exception.exceptions.BotException;
import com.orgtgbot.exception.exceptions.gemini.DeserializeGeminiResponse;
import com.orgtgbot.exception.exceptions.gemini.GeminiParseTextException;
import com.orgtgbot.exception.exceptions.gemini.GeminiParseVoiceException;
import com.orgtgbot.exception.exceptions.http.SendHttpRequestToGeminiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final TelegramSender sender;

    private static final Map<Class<? extends Exception>, String> ERROR_MESSAGES = Map.of(
            GeminiParseTextException.class, "Fail, try create remind again.",
            GeminiParseVoiceException.class, "Fail, try create remind by text.",
            SendHttpRequestToGeminiException.class, "Fail, from AI side.",
            DeserializeGeminiResponse.class, "Fail, from AI side."
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
