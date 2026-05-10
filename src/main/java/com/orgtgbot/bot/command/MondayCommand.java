package com.orgtgbot.bot.command;

import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MondayCommand implements CommandHandler {

    private final ProbegService probegService;

    @Override
    public String name() {
        return "monday";
    }

    @Override
    public String execute(Update update) {
        String[] parts = update.getMessage().getText().trim().split("\\s+");
        if (parts.length != 2 || !parts[1].matches("\\d+"))
            return "укажите кол-во км например 12";
        return probegService.changeMonday(List.of(Integer.parseInt(parts[1])));
    }
}
