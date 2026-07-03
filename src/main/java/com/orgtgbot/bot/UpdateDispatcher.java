package com.orgtgbot.bot;

import com.orgtgbot.bot.update.UpdateHandler;
import com.orgtgbot.exception.exceptions.bot.FailedExtractingException;
import com.orgtgbot.exception.exceptions.bot.UnknownUpdateHandlerException;
import com.orgtgbot.service.services.user.UserStateService;
import com.orgtgbot.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final UserEntryRepository userEntryRepository;
    private final UserStateService userStateService;
    private final List<UpdateHandler> handlers;

    public void dispatch(Update update) {
        Long chatId = fieldExtractor(update, MaybeInaccessibleMessage::getChatId)
                .orElseThrow(() -> new FailedExtractingException("ChatId not found"));
        boolean isUserRegistered = userEntryRepository.existsByTelegramChatId(chatId);

        fieldExtractor(update, MaybeInaccessibleMessage::getDate).ifPresent(dateSeconds -> {
            if (isUserRegistered) {
                LocalDateTime userActionTime = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(dateSeconds), ZoneId.of("Europe/Kiev")
                );
                userStateService.updateLastActivityTime(chatId, userActionTime);
            }
        });

        handlers.stream()
                .filter(handler -> handler.canHandle(update, isUserRegistered))
                .findFirst()
                .orElseThrow(() -> new UnknownUpdateHandlerException("Requested handler not found"))
                .handle(update, chatId);
    }

    private <T> Optional<T> fieldExtractor(Update update, Function<MaybeInaccessibleMessage, T> extractor) {
        if (update.hasMessage()) return Optional.ofNullable(update.getMessage()).map(extractor);
        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null) {
            return Optional.ofNullable(update.getCallbackQuery().getMessage()).map(extractor);
        }
        return Optional.empty();
    }
}
