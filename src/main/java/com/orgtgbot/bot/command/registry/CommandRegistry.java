package com.orgtgbot.bot.command.registry;

import com.orgtgbot.bot.command.CommandHandler;
import com.orgtgbot.bot.command.UnknownCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommandRegistry {

    private final Map<String, CommandHandler> handlers;
    private final UnknownCommand fallback;

    public CommandRegistry(List<CommandHandler> all, UnknownCommand fallback) {
        this.fallback = fallback;
        this.handlers = all.stream()
                .filter(h -> h.name() != null)
                .collect(Collectors.toUnmodifiableMap(CommandHandler::name, h -> h));
    }

    public CommandHandler resolve(String text) {
        if (text == null || !text.startsWith("/"))
            return fallback;
        String name = text.substring(1).split("\\s+", 2)[0];
        return handlers.getOrDefault(name, fallback);
    }
}
