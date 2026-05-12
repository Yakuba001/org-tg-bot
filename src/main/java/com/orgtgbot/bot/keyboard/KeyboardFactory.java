package com.orgtgbot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.orgtgbot.bot.keyboard.Buttons.*;

public class KeyboardFactory {

    public static InlineKeyboardMarkup mainMenu() {
        return create(MAIN_MENU.getName(), MAIN_MENU.name());
    }

    public static InlineKeyboardMarkup probegMenu() {
        return create(
                PROBEG_MONDAY.getName(), PROBEG_MONDAY.name(),
                GET_REPORT.getName(), GET_REPORT.name(),
                BACK.getName(), MAIN_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegMonday() {
        return create(
                SET_MORNING_KM.getName(), SET_MORNING_KM.name(),
                SET_EVENING_KM.getName(), SET_EVENING_KM.name(),
                BACK.getName(), PROBEG_MENU.name()
        );
    }

    private static InlineKeyboardMarkup create(String... data) {
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
