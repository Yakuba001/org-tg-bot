package com.orgtgbot.bot.state;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface StateHandler {

    UserState getSupportedHandle();
    void handle(Update update);
}
