package com.orgtgbot.bot.keyboard;

import com.orgtgbot.bot.callback.GeneralFields;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.orgtgbot.bot.callback.GeneralFields.*;

public class KeyboardFactory {

    public static InlineKeyboardMarkup mainMenu() {
        return create(PROBEG_MENU.getDescription(), PROBEG_MENU.name());
    }

    public static InlineKeyboardMarkup probegMenu() {
        return create(
                PROBEG_MONDAY.getDescription(), PROBEG_MONDAY.name(),
                PROBEG_TUESDAY.getDescription(), PROBEG_TUESDAY.name(),
                PROBEG_WEDNESDAY.getDescription(), PROBEG_WEDNESDAY.name(),
                PROBEG_THURSDAY.getDescription(), PROBEG_THURSDAY.name(),
                PROBEG_FRIDAY.getDescription(), PROBEG_FRIDAY.name(),
                GENERAL.getDescription(), GENERAL.name(),
                GET_REPORT.getDescription(), GET_REPORT.name(),
                CLEAR_ALL.getDescription(), CLEAR_ALL.name(),
                BACK.getDescription(), MAIN_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegMondayMenu() {
        return create(
                SET_MORNING_MONDAY_KM.getDescription(), SET_MORNING_MONDAY_KM.name(),
                SET_EVENING_MONDAY_KM.getDescription(), SET_EVENING_MONDAY_KM.name(),
                SET_TOTAL_MONDAY_KM.getDescription(), SET_TOTAL_MONDAY_KM.name(),
                SET_MONDAY_ROUTE.getDescription(), SET_MONDAY_ROUTE.name(),
                MONDAY_DATE.getDescription(), MONDAY_DATE.name(),
                BACK.getDescription(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegTuesdayMenu() {
        return create(
                SET_MORNING_TUESDAY_KM.getDescription(), SET_MORNING_TUESDAY_KM.name(),
                SET_EVENING_TUESDAY_KM.getDescription(), SET_EVENING_TUESDAY_KM.name(),
                SET_TOTAL_TUESDAY_KM.getDescription(), SET_TOTAL_TUESDAY_KM.name(),
                SET_TUESDAY_ROUTE.getDescription(), SET_TUESDAY_ROUTE.name(),
                TUESDAY_DATE.getDescription(), TUESDAY_DATE.name(),
                BACK.getDescription(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegWednesdayMenu() {
        return create(
                SET_MORNING_WEDNESDAY_KM.getDescription(), SET_MORNING_WEDNESDAY_KM.name(),
                SET_EVENING_WEDNESDAY_KM.getDescription(), SET_EVENING_WEDNESDAY_KM.name(),
                SET_TOTAL_WEDNESDAY_KM.getDescription(), SET_TOTAL_WEDNESDAY_KM.name(),
                SET_WEDNESDAY_ROUTE.getDescription(), SET_WEDNESDAY_ROUTE.name(),
                WEDNESDAY_DATE.getDescription(), WEDNESDAY_DATE.name(),
                BACK.getDescription(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegThursdayMenu() {
        return create(
                SET_MORNING_THURSDAY_KM.getDescription(), SET_MORNING_THURSDAY_KM.name(),
                SET_EVENING_THURSDAY_KM.getDescription(), SET_EVENING_THURSDAY_KM.name(),
                SET_TOTAL_THURSDAY_KM.getDescription(), SET_TOTAL_THURSDAY_KM.name(),
                SET_THURSDAY_ROUTE.getDescription(), SET_THURSDAY_ROUTE.name(),
                THURSDAY_DATE.getDescription(), THURSDAY_DATE.name(),
                BACK.getDescription(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probeFridayMenu() {
        return create(
                SET_MORNING_FRIDAY_KM.getDescription(), SET_MORNING_FRIDAY_KM.name(),
                SET_EVENING_FRIDAY_KM.getDescription(), SET_EVENING_FRIDAY_KM.name(),
                SET_TOTAL_FRIDAY_KM.getDescription(), SET_TOTAL_FRIDAY_KM.name(),
                SET_FRIDAY_ROUTE.getDescription(), SET_FRIDAY_ROUTE.name(),
                FRIDAY_DATE.getDescription(), FRIDAY_DATE.name(),
                BACK.getDescription(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup clearAllMenu() {
        return create(
                ACCEPT_CLEAR.getDescription(), ACCEPT_CLEAR.name(),
                DECLINE_CLEAR.getDescription(), DECLINE_CLEAR.name()
        );
    }

    public static InlineKeyboardMarkup generalMenu() {
        return create(
                DRIVER.getDescription(), DRIVER.name(),
                DATE.getDescription(), DATE.name(),
                MODEL_AUTO.getDescription(), MODEL_AUTO.name(),
                NUMBER_AUTO.getDescription(), NUMBER_AUTO.name(),
                START_WEEK_PROBEG.getDescription(), START_WEEK_PROBEG.name(),
                END_WEEK_PROBEG.getDescription(), END_WEEK_PROBEG.name(),
                START_BALANCE_LITRES.getDescription(), START_BALANCE_LITRES.name(),
                END_BALANCE_LITRES.getDescription(), END_BALANCE_LITRES.name(),
                TOTAL_WEEK_KM.getDescription(), TOTAL_WEEK_KM.name(),
                FUEL_NORM.getDescription(), FUEL_NORM.name(),
                LITRES_SPEND.getDescription(), LITRES_SPEND.name(),
                FUELING.getDescription(), FUELING.name(),
                BACK.getDescription(), PROBEG_MENU.name()
        );
    }

    public static InlineKeyboardMarkup probegBack(GeneralFields field) {
        switch (field) {
            case SET_MORNING_MONDAY_KM, SET_EVENING_MONDAY_KM, SET_TOTAL_MONDAY_KM, SET_MONDAY_ROUTE, MONDAY_DATE -> {
                return create(
                        BACK.getDescription(), PROBEG_MONDAY.name()
                );
            }
            case SET_MORNING_TUESDAY_KM, SET_EVENING_TUESDAY_KM, SET_TOTAL_TUESDAY_KM, SET_TUESDAY_ROUTE, TUESDAY_DATE -> {
                return create(
                        BACK.getDescription(), PROBEG_TUESDAY.name()
                );
            }
            case SET_MORNING_WEDNESDAY_KM, SET_EVENING_WEDNESDAY_KM, SET_TOTAL_WEDNESDAY_KM, SET_WEDNESDAY_ROUTE, WEDNESDAY_DATE -> {
                return create(
                        BACK.getDescription(), PROBEG_WEDNESDAY.name()
                );
            }
            case SET_MORNING_THURSDAY_KM, SET_EVENING_THURSDAY_KM, SET_TOTAL_THURSDAY_KM, SET_THURSDAY_ROUTE, THURSDAY_DATE -> {
                return create(
                        BACK.getDescription(), PROBEG_THURSDAY.name()
                );
            }
            case SET_MORNING_FRIDAY_KM, SET_EVENING_FRIDAY_KM, SET_TOTAL_FRIDAY_KM, SET_FRIDAY_ROUTE, FRIDAY_DATE -> {
                return create(
                        BACK.getDescription(), PROBEG_FRIDAY.name()
                );
            }
            default -> {
                return create(
                        BACK.getDescription(), PROBEG_MENU.name()
                );
            }
        }
    }

    public static InlineKeyboardMarkup generalBack() {
        return create(
                BACK.getDescription(), GENERAL.name()
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
