package com.orgtgbot.bot.update;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.service.services.user.UserStateService;
import com.orgtgbot.service.services.voice.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@RequiredArgsConstructor
public class VoiceUpdateHandler implements UpdateHandler {

    private final VoiceService voiceService;
    private final UserStateService userStateService;
    private final TelegramSender sender;

    @Override
    public boolean canHandle(Update update, boolean isUserRegistered) {
        return update.hasMessage() && update.getMessage().hasVoice() && isUserRegistered;
    }

    @Override
    public void handle(Update update, Long chatId) throws Exception {
        Message message = update.getMessage();
        Integer messageId = message.getMessageId();
        GeneralFields currentField = userStateService.getState(chatId);

        if (currentField == GeneralFields.MAIN_REMINDER) {
            Integer botMenuId = userStateService.getMessageId(chatId);
            voiceService.handleVoiceAsync(chatId, message.getVoice(), botMenuId, currentField);
        } else {
            sender.sendMessage(chatId, "❌ В этом разделе голосовые сообщения не поддерживаются.");
        }
        sender.deleteMessage(chatId, messageId);
    }
}
