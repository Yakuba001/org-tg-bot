package com.orgtgbot.bot.command;

import com.orgtgbot.service.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProbegCommand implements CommandHandler {

    private final ProbegService probegService;

    @Override
    public String name() { return "probeg"; }

    @Override
    public String execute(Update update) {
        String[] parts = update.getMessage().getText().trim().split("\\s+");

        if (parts.length < 2) {
            return "Укажи пробег через пробел. Пример:\n/probeg 14 52 64";
        }
        if (parts.length - 1 > 5) {
            return "Максимум 5 значений.";
        }

        try {
            List<Integer> km = Arrays.stream(parts)
                    .skip(1)
                    .map(Integer::parseInt)
                    .toList();

            probegService.saveMileage(km);

            String[] days = {"Пн", "Вт", "Ср", "Чт", "Пт"};
            StringBuilder sb = new StringBuilder("✅ Сохранено:\n");
            for (int i = 0; i < km.size(); i++) {
                sb.append(days[i]).append(": ").append(km.get(i)).append(" км\n");
            }
            sb.append("\n/get_report — получить Excel");
            return sb.toString();

        } catch (NumberFormatException e) {
            return "Ошибка: только целые числа. Пример:\n/probeg 14 52 64";
        }
    }
}
