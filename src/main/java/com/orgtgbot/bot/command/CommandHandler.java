package com.orgtgbot.bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {

    String name();
    String execute(Update update);
}
