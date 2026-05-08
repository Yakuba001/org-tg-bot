package com.orgtgbot.bot.command;

import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

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
        if (parts.length < 2) {
            return "укажите кол-во км например 12";
        } else if (parts.length > 2) {
            return "укажите кол-во км например 12";
        } else {
            try {
                List<Integer> result = Arrays.stream(parts).map(Integer::parseInt).toList();
                probegService.changeMonday(result);
                return "Monday:" + parts[1] + " km." + "\n" + probegService.getAll().stream()
                        .map(e -> e.getRowNumber() + ": " + e.getKilometers() + " km.").toList();
            } catch (NumberFormatException e) {
                return "Ошибка: только целые числа. Например: /monday 12";
            }
        }
    }
}
