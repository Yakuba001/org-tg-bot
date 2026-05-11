package com.orgtgbot.bot;

import com.orgtgbot.config.TelegramBotProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class OrganizerBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramBotProperties props;
    private final UpdateDispatcher dispatcher;

    @Override public String getBotToken() {
        return props.token();
    }

    @Override public LongPollingSingleThreadUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        try {
            dispatcher.dispatch(update);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
