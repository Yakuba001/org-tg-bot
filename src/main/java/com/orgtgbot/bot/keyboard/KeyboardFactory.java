package com.orgtgbot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Arrays;
import java.util.List;

import static com.orgtgbot.bot.keyboard.Buttons.*;

public class KeyboardFactory {

    public static InlineKeyboardMarkup mainMenu() {
        return verticalKeyboard(
                button(" Пробег", PROBEG_MENU.name())
        );
    }

    public static InlineKeyboardMarkup probegMenu() {
        return verticalKeyboard(
                button(" Понедельник", PROBEG_MONDAY.name()),
                button(" Получить отчёт", GET_REPORT.name()),
                button(" Назад", MAIN_MENU.name())
        );
    }

    public static InlineKeyboardMarkup probegMonday() {
        return verticalKeyboard(
                button(" Назад", PROBEG_MENU.name())
        );
    }

    private static InlineKeyboardMarkup verticalKeyboard(InlineKeyboardButton... buttons) {
        List<InlineKeyboardRow> rows = Arrays.stream(buttons)
                .map(InlineKeyboardRow::new)
                .toList();
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    private static InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }
}
