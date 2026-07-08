package com.orgtgbot.bot.update;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.bot.message.MessageType;
import com.orgtgbot.repository.InviteCodeRepository;
import com.orgtgbot.service.services.message.MessageLogService;
import com.orgtgbot.service.services.user.RegistrationService;
import com.orgtgbot.service.services.user.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Collection;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UnregisteredTextUpdateHandler implements UpdateHandler {

    private final UserStateService userStateService;
    private final InviteCodeRepository inviteCodeRepository;
    private final RegistrationService registrationService;
    private final StartCommand startCommand;
    private final TelegramSender sender;
    private final MessageLogService messageLogService;

    @Override
    public boolean canHandle(Update update, boolean isUserRegistered) {
        return update.hasMessage() && update.getMessage().hasText() && !isUserRegistered;
    }

    @Override
    public void handle(Update update, Long chatId) {
        Message message = update.getMessage();
        Integer messageId = message.getMessageId();
        String text = message.getText().trim();

        if (text.startsWith("/start")) {
            userStateService.setState(chatId, GeneralFields.NONE);
            sender.deleteMessage(chatId, messageId);
            sender.sendMessage(chatId, "🔒 Доступ ограничен. Введите секретный код для авторизации:");
            return;
        }

        if (inviteCodeRepository.existsByCode(text)) {
            registrationService.registerNewDriver(chatId, message.getFrom().getFirstName());
            Stream.of(
                            messageLogService.deleteMessagesByType(chatId, MessageType.USER_INPUT),
                            messageLogService.deleteMessagesByType(chatId, MessageType.BOT_TEXT)
                    )
                    .flatMap(Collection::stream)
                    .forEach(e -> {
                        try {
                            sender.deleteMessage(chatId, e.getMessageId());
                        } catch (Exception ignore) {
                        }
                    });
            startCommand.execute(update);
        } else {
            sender.deleteMessage(chatId, messageId);
            sender.sendMessage(chatId, "🔒 Неверный код. Доступ ограничен:");
        }
    }
}
