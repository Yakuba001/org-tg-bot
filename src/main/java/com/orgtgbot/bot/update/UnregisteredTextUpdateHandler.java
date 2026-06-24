package com.orgtgbot.bot.update;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.repository.InviteCodeRepository;
import com.orgtgbot.service.services.user.RegistrationService;
import com.orgtgbot.service.services.user.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@RequiredArgsConstructor
public class UnregisteredTextUpdateHandler implements UpdateHandler {

    private final UserStateService userStateService;
    private final InviteCodeRepository inviteCodeRepository;
    private final RegistrationService registrationService;
    private final StartCommand startCommand;
    private final TelegramSender sender;

    @Override
    public boolean canHandle(Update update, boolean isUserRegistered) {
        return update.hasMessage() && update.getMessage().hasText() && !isUserRegistered;
    }

    @Override
    public void handle(Update update, Long chatId) throws Exception {
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
            startCommand.execute(update);
        } else {
            sender.deleteMessage(chatId, messageId);
            sender.sendMessage(chatId, "🔒 Неверный код. Доступ ограничен:");
        }
    }
}
