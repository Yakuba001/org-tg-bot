package com.orgtgbot.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartCommand implements CommandHandler {

    @Override
    public String name() {
        return "start";
    }

    @Override
    public String execute(Update update) {
        return """
                Привет! 👋 Я бот-органайзер.
                Команды:
                /ping — проверка связи
                """;
    }
}
