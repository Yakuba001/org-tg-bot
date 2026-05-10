package com.orgtgbot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.orgtgbot.bot.keyboard.Buttons.*;

public class KeyboardFactory {

    public static InlineKeyboardMarkup mainMenu() {
        return create(" Пробег", PROBEG_MENU.name());
    }

    public static InlineKeyboardMarkup probegMenu() {
        return create(
                " Понедельник", PROBEG_MONDAY.name(),
                " Получить отчёт", GET_REPORT.name(),
                " Назад", MAIN_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegMonday() {
        return create(" Назад", PROBEG_MENU.name());
    }

    public static InlineKeyboardMarkup create(String... data) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < data.length; i += 2) {
            rows.add(new InlineKeyboardRow(
                    InlineKeyboardButton.builder()
                            .text(data[i])
                            .callbackData(data[i + 1])
                            .build()
            ));
        }
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }
}
