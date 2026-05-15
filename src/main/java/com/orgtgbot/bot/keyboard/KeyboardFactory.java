package com.orgtgbot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.orgtgbot.bot.keyboard.Buttons.*;
import static com.orgtgbot.bot.keyboard.Buttons.SET_EVENING_THURSDAY_KM;
import static com.orgtgbot.bot.keyboard.Buttons.SET_EVENING_TUESDAY_KM;
import static com.orgtgbot.bot.keyboard.Buttons.SET_EVENING_WEDNESDAY_KM;

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
                GENERAL.getName(), GENERAL.name(),
                GET_REPORT.getName(), GET_REPORT.name(),
                CLEAR_ALL.getName(), CLEAR_ALL.name(),
                BACK.getName(), MAIN_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegMondayMenu() {
        return create(
                SET_MORNING_MONDAY_KM.getName(), SET_MORNING_MONDAY_KM.name(),
                SET_EVENING_MONDAY_KM.getName(), SET_EVENING_MONDAY_KM.name(),
                SET_TOTAL_MONDAY_KM.getName(), SET_TOTAL_MONDAY_KM.name(),
                SET_MONDAY_ROUTE.getName(), SET_MONDAY_ROUTE.name(),
                MONDAY_DATE.getName(), MONDAY_DATE.name(),
                BACK.getName(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegTuesdayMenu() {
        return create(
                SET_MORNING_TUESDAY_KM.getName(), SET_MORNING_TUESDAY_KM.name(),
                SET_EVENING_TUESDAY_KM.getName(), SET_EVENING_TUESDAY_KM.name(),
                SET_TOTAL_TUESDAY_KM.getName(), SET_TOTAL_TUESDAY_KM.name(),
                SET_TUESDAY_ROUTE.getName(), SET_TUESDAY_ROUTE.name(),
                TUESDAY_DATE.getName(), TUESDAY_DATE.name(),
                BACK.getName(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegWednesdayMenu() {
        return create(
                SET_MORNING_WEDNESDAY_KM.getName(), SET_MORNING_WEDNESDAY_KM.name(),
                SET_EVENING_WEDNESDAY_KM.getName(), SET_EVENING_WEDNESDAY_KM.name(),
                SET_TOTAL_WEDNESDAY_KM.getName(), SET_TOTAL_WEDNESDAY_KM.name(),
                SET_WEDNESDAY_ROUTE.getName(), SET_WEDNESDAY_ROUTE.name(),
                WEDNESDAY_DATE.getName(), WEDNESDAY_DATE.name(),
                BACK.getName(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegThursdayMenu() {
        return create(
                SET_MORNING_THURSDAY_KM.getName(), SET_MORNING_THURSDAY_KM.name(),
                SET_EVENING_THURSDAY_KM.getName(), SET_EVENING_THURSDAY_KM.name(),
                SET_TOTAL_THURSDAY_KM.getName(), SET_TOTAL_THURSDAY_KM.name(),
                SET_THURSDAY_ROUTE.getName(), SET_THURSDAY_ROUTE.name(),
                THURSDAY_DATE.getName(), THURSDAY_DATE.name(),
                BACK.getName(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probeFridayMenu() {
        return create(
                SET_MORNING_FRIDAY_KM.getName(), SET_MORNING_FRIDAY_KM.name(),
                SET_EVENING_FRIDAY_KM.getName(), SET_EVENING_FRIDAY_KM.name(),
                SET_TOTAL_FRIDAY_KM.getName(), SET_TOTAL_FRIDAY_KM.name(),
                SET_FRIDAY_ROUTE.getName(), SET_FRIDAY_ROUTE.name(),
                FRIDAY_DATE.getName(), FRIDAY_DATE.name(),
                BACK.getName(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup clearAllMenu() {
        return create(
                ACCEPT_CLEAR.getName(), ACCEPT_CLEAR.name(),
                DECLINE_CLEAR.getName(), DECLINE_CLEAR.name()
        );
    }

    public static InlineKeyboardMarkup generalMenu() {
        return create(
                DRIVER.getName(), DRIVER.name(),
                DATA.getName(), DATA.name(),
                MODEL_AUTO.getName(), MODEL_AUTO.name(),
                NUMBER_AUTO.getName(), NUMBER_AUTO.name(),
                START_WEEK_PROBEG.getName(), START_WEEK_PROBEG.name(),
                END_WEEK_PROBEG.getName(), END_WEEK_PROBEG.name(),
                START_BALANCE_LITRES.getName(), START_BALANCE_LITRES.name(),
                END_BALANCE_LITRES.getName(), END_BALANCE_LITRES.name(),
                TOTAL_WEEK_KM.getName(), TOTAL_WEEK_KM.name(),
                FUEL_NORM.getName(), FUEL_NORM.name(),
                LITRES_SPEND.getName(), LITRES_SPEND.name(),
                FUELING.getName(), FUELING.name(),
                BACK.getName(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegBack(Buttons button) {
        switch (button) {
            case SET_MORNING_MONDAY_KM, SET_EVENING_MONDAY_KM, SET_TOTAL_MONDAY_KM, SET_MONDAY_ROUTE, MONDAY_DATE -> {
                return create(
                        BACK.getName(), PROBEG_MONDAY.name()
                );
            }
            case SET_MORNING_TUESDAY_KM, SET_EVENING_TUESDAY_KM, SET_TOTAL_TUESDAY_KM, SET_TUESDAY_ROUTE, TUESDAY_DATE -> {
                return create(
                        BACK.getName(), PROBEG_TUESDAY.name()
                );
            }
            case SET_MORNING_WEDNESDAY_KM, SET_EVENING_WEDNESDAY_KM, SET_TOTAL_WEDNESDAY_KM, SET_WEDNESDAY_ROUTE, WEDNESDAY_DATE -> {
                return create(
                        BACK.getName(), PROBEG_WEDNESDAY.name()
                );
            }
            case SET_MORNING_THURSDAY_KM, SET_EVENING_THURSDAY_KM, SET_TOTAL_THURSDAY_KM, SET_THURSDAY_ROUTE, THURSDAY_DATE -> {
                return create(
                        BACK.getName(), PROBEG_THURSDAY.name()
                );
            }
            case SET_MORNING_FRIDAY_KM, SET_EVENING_FRIDAY_KM, SET_TOTAL_FRIDAY_KM, SET_FRIDAY_ROUTE, FRIDAY_DATE -> {
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

    public static InlineKeyboardMarkup generalBack() {
        return create(
                BACK.getName(), GENERAL.name()
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
