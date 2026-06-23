package com.orgtgbot.bot;

import com.orgtgbot.service.services.user.UserStateService;
import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.repository.InviteCodeRepository;
import com.orgtgbot.repository.UserEntryRepository;
import com.orgtgbot.service.services.user.RegistrationService;
import com.orgtgbot.service.voice.VoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final StartCommand startCommand;
    private final CallbackRegistry callbackRegistry;
    private final UserStateService userStateService;
    private final TelegramSender sender;
    private final UserEntryRepository userEntryRepository;
    private final InviteCodeRepository inviteCodeRepository;
    private final RegistrationService registrationService;
    private final VoiceService voiceService;

    public void dispatch(Update update) throws Exception {
        Long chatId = fieldExtractor(update, MaybeInaccessibleMessage::getChatId)
                .orElseThrow(() -> new IllegalStateException("ChatId not found"));
        boolean isUserRegistered = userEntryRepository.existsByTelegramChatId(chatId);

        fieldExtractor(update, MaybeInaccessibleMessage::getDate).ifPresent(dateSeconds -> {
            if (isUserRegistered) {
                LocalDateTime userActionTime = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(dateSeconds), ZoneId.of("Europe/Kiev")
                );
                userStateService.updateLastActivityTime(chatId, userActionTime);
            }
        });

        if (update.hasMessage()) {
            handleMessageUpdate(update, chatId, isUserRegistered);
        } else if (update.hasCallbackQuery() && isUserRegistered) {
            handleCallbackUpdate(update, chatId);
        }
    }

    private void handleMessageUpdate(Update update, Long chatId, boolean isUserRegistered) throws Exception {
        Message message = update.getMessage();
        if (message == null || (!message.hasText() && !message.hasVoice())) return;

        Integer messageId = message.getMessageId();

        if (!isUserRegistered) {
            if (message.hasText()) {
                processUnregisteredText(update, chatId, messageId);
            } else {
                sender.deleteMessage(chatId, messageId);
                sender.sendMessage(chatId, "🔒 Доступ ограничен. Авторизация возможна только текстом.");
            }
            return;
        }

        if (message.hasVoice()) {
            processVoice(chatId, message.getVoice(), messageId);
        } else if (message.hasText()) {
            processRegisteredText(update, chatId, messageId);
        }
    }

    private void processUnregisteredText(Update update, Long chatId, Integer messageId) throws Exception {
        String text = update.getMessage().getText().trim();
        if (text.startsWith("/start")) {
            userStateService.setState(chatId, GeneralFields.NONE);
            sender.deleteMessage(chatId, messageId);
            sender.sendMessage(chatId, "🔒 Доступ ограничен. Введите секретный код для авторизации:");
            return;
        }

        if (inviteCodeRepository.existsByCode(text)) {
            registrationService.registerNewDriver(chatId, update.getMessage().getFrom().getFirstName());
            startCommand.execute(update);
        } else {
            sender.deleteMessage(chatId, messageId);
            sender.sendMessage(chatId, "🔒 Неверный код. Доступ ограничен:");
        }
    }

    private void processRegisteredText(Update update, Long chatId, Integer messageId) throws Exception {
        String text = update.getMessage().getText().trim();
        GeneralFields currentField = userStateService.getState(chatId);

        sender.deleteMessage(chatId, messageId);

        if (text.startsWith("/start") || currentField == null || currentField == GeneralFields.NONE) {
            startCommand.execute(update);
        } else {
            callbackRegistry.handle(currentField, chatId, text, userStateService.getMessageId(chatId));
        }
    }

    private void processVoice(Long chatId, Voice voice, Integer messageId) {
        GeneralFields currentField = userStateService.getState(chatId);
        if (currentField == GeneralFields.MAIN_REMINDER) {
            voiceService.handleVoice(chatId, voice);
        } else {
            sender.sendMessage(chatId, "❌ В этом разделе голосовые сообщения не поддерживаются.");
        }
        sender.deleteMessage(chatId, messageId);
    }

    private void handleCallbackUpdate(Update update, Long chatId) throws Exception {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        GeneralFields clickedField = GeneralFields.valueOf(callbackQuery.getData());
        Integer callbackMessageId = callbackQuery.getMessage().getMessageId();
        Integer botMenuId = userStateService.getMessageId(chatId);
        callbackRegistry.dispatch(clickedField, callbackQuery, chatId, callbackMessageId, botMenuId);
    }

    private <T> Optional<T> fieldExtractor(Update update, Function<MaybeInaccessibleMessage, T> extractor) {
        if (update.hasMessage()) return Optional.ofNullable(update.getMessage()).map(extractor);
        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null) {
            return Optional.ofNullable(update.getCallbackQuery().getMessage()).map(extractor);
        }
        return Optional.empty();
    }
}
