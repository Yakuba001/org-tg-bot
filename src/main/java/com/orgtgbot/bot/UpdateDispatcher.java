package com.orgtgbot.bot;

import com.orgtgbot.bot.callback.CallbackRegistry;
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
    private final CallbackRegistry callbackRegistry;
    private final TelegramSender sender;

    public void dispatch(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            log.debug("chat={} text={}", chatId, text);

            CommandHandler handler = registry.resolve(text);
            String response = handler.execute(update);
            if (response != null && !response.isBlank()) {
                sender.sendText(chatId, response);
            }
        }

        if (update.hasCallbackQuery()) {
            log.debug("callback={}", update.getCallbackQuery().getData());
            callbackRegistry.dispatch(update.getCallbackQuery());
        }
    }
}
