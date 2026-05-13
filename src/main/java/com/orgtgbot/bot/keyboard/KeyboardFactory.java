package com.orgtgbot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.orgtgbot.bot.keyboard.Buttons.*;

public class KeyboardFactory {

    public static InlineKeyboardMarkup mainMenu() {
        return create(PROBEG_MENU.getName(), PROBEG_MENU.name());
    }

    public static InlineKeyboardMarkup probegMenu() {
        return create(
                PROBEG_MONDAY.getName(), PROBEG_MONDAY.name(),
                PROBEG_TUESDAY.getName(), PROBEG_TUESDAY.name(),
                PROBEG_WEDNESDAY.getName(), PROBEG_WEDNESDAY.name(),
                PROBEG_THURSDAY.getName(), PROBEG_THURSDAY.name(),
                PROBEG_FRIDAY.getName(), PROBEG_FRIDAY.name(),
                GET_REPORT.getName(), GET_REPORT.name(),
                BACK.getName(), MAIN_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegDayMenu() {
        return create(
                SET_MORNING_KM.getName(), SET_MORNING_KM.name(),
                SET_EVENING_KM.getName(), SET_EVENING_KM.name(),
                SET_TOTAL_KM.getName(), SET_TOTAL_KM.name(),
                SET_ROUTE.getName(), SET_ROUTE.name(),
                BACK.getName(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegBack(Buttons button) {
        switch (button) {
            case PROBEG_MONDAY -> {
                return create(
                        BACK.getName(), PROBEG_MONDAY.name()
                );
            }
            case PROBEG_TUESDAY -> {
                return create(
                        BACK.getName(), PROBEG_TUESDAY.name()
                );
            }
            case PROBEG_WEDNESDAY -> {
                return create(
                        BACK.getName(), PROBEG_WEDNESDAY.name()
                );
            }
            case PROBEG_THURSDAY -> {
                return create(
                        BACK.getName(), PROBEG_THURSDAY.name()
                );
            }
            case PROBEG_FRIDAY -> {
                return create(
                        BACK.getName(), PROBEG_FRIDAY.name()
                );
            }
            default -> {
                return create(
                        BACK.getName(), PROBEG_MENU.name()
                );
            }
        }
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
