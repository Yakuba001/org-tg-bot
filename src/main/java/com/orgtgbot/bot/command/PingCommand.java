package com.orgtgbot.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class PingCommand implements CommandHandler {

    @Override
    public String name() {
        return "ping";
    }

    @Override
    public String execute(Update update) {
        return "pong";
    }
}
