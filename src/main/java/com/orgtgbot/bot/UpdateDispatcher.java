package com.orgtgbot.bot;

import com.orgtgbot.bot.command.CommandHandler;
import com.orgtgbot.bot.command.registry.CommandRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final CommandRegistry registry;
    private final TelegramSender sender;

    public void dispatch(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        log.debug("chat={} text={}", chatId, text);

        CommandHandler handler = registry.resolve(text);
        sender.sendText(chatId, handler.execute(update));
    }
}
