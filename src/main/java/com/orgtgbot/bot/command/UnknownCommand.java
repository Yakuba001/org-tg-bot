package com.orgtgbot.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UnknownCommand implements CommandHandler {

    @Override
    public String name() {
        return null;
    }

    @Override
    public String execute(Update update) {
        return "Не знаю такой команды. Попробуй /start";
    }
}
