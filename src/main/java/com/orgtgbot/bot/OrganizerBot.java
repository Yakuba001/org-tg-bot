package com.orgtgbot.bot;

import com.orgtgbot.bot.util.FieldExtractor;
import com.orgtgbot.config.TelegramBotProperties;
import com.orgtgbot.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizerBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramBotProperties props;
    private final UpdateDispatcher dispatcher;
    private final GlobalExceptionHandler globalExceptionHandler;

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public String getBotToken() {
        return props.token();
    }

    @Override
    public LongPollingSingleThreadUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        executor.execute(() -> {
            try {
                dispatcher.dispatch(update);
            } catch (Exception e) {
                globalExceptionHandler.handle(e,
                        FieldExtractor.extractChatId(update),
                        FieldExtractor.extractMessageId(update));
            }
        });
    }
}
