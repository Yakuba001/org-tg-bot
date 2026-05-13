package com.orgtgbot.bot.keyboard;

import lombok.Getter;

@Getter
public enum Buttons {
    MAIN_MENU("Главное меню"),
    PROBEG_MENU("Меню пробега"),
    PROBEG_MONDAY("Понедельник"),
    PROBEG_TUESDAY("Вторник"),
    PROBEG_WEDNESDAY("Среда"),
    PROBEG_THURSDAY("Четверг"),
    PROBEG_FRIDAY("Пятница"),
    GET_REPORT("Получить отчёт"),
    SET_MORNING_MONDAY_KM("Утро"),
    SET_EVENING_MONDAY_KM("Вечер"),
    SET_TOTAL_MONDAY_KM("Тотал"),
    SET_MONDAY_ROUTE("Маршрут"),
    BACK("Назад");

    private final String name;

    Buttons(String name) {
        this.name = name;
    }
}
