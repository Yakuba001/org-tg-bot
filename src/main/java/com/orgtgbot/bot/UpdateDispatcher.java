package com.orgtgbot.bot;

import com.orgtgbot.service.services.user.UserStateService;
import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.repository.InviteCodeRepository;
import com.orgtgbot.repository.UserEntryRepository;
import com.orgtgbot.service.services.user.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    public void dispatch(Update update) throws Exception {
        Long chatId = extractChatId(update);
        if (chatId == null) return;
        boolean isUserRegistered = userEntryRepository.existsByTelegramChatId(chatId);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().trim();
            Integer messageId = update.getMessage().getMessageId();
            if (!isUserRegistered && text.startsWith("/start")) {
                userStateService.setState(chatId, GeneralFields.NONE);
                sender.deleteMessage(chatId, messageId);
                sender.sendMessage(chatId, "🔒 Доступ ограничен. Введите секретный код для авторизации:");
                return;
            }
            if (!isUserRegistered) {
                sender.deleteMessage(chatId, messageId);
                if (inviteCodeRepository.existsByCode(text)) {
                    registrationService.registerNewDriver(chatId, update.getMessage().getFrom().getFirstName());
                    startCommand.execute(update);
                } else {
                    log.warn("Неверный код авторизации: '{}'", text);
                }
                return;
            }
            if (text.startsWith("/start")) {
                startCommand.execute(update);
                sender.deleteMessage(chatId, messageId);
            } else {
                GeneralFields currentField = userStateService.getState(chatId);
                log.info("===> ДИСПЕТЧЕР ПРОВЕРЯЕТ СТЕЙТ ПЕРЕД ВВОДОМ ТЕКСТА: '{}'", currentField);


                if (currentField == null || currentField == GeneralFields.NONE) {
                    startCommand.execute(update);
                    sender.deleteMessage(chatId, messageId);
                } else {
                    callbackRegistry.handle(currentField, chatId, text, userStateService.getMessageId(chatId));
                    sender.deleteMessage(chatId, messageId);
                }
            }
            return;
        }
        if (update.hasCallbackQuery() && isUserRegistered) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            GeneralFields clickedField = GeneralFields.valueOf(callbackQuery.getData());
            Integer callbackMessageId = callbackQuery.getMessage().getMessageId();
            Integer botMenuId = userStateService.getMessageId(chatId);
            callbackRegistry.dispatch(clickedField, callbackQuery, chatId, callbackMessageId, botMenuId);
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
