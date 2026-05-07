package com.orgtgbot.bot;

import com.orgtgbot.config.TelegramBotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class TelegramSender {

    private final TelegramClient client;

    public TelegramSender(TelegramBotProperties props) {
        this.client = new OkHttpTelegramClient(props.token());
    }

    public void sendText(Long chatId, String text) {
        try {
            client.execute(SendMessage.builder()
                    .chatId(chatId).text(text).build());
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить сообщение в чат {}", chatId, e);
        }
    }
}
