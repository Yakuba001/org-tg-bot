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
    SET_MORNING_TUESDAY_KM("Утро"),
    SET_EVENING_TUESDAY_KM("Вечер"),
    SET_TOTAL_TUESDAY_KM("Тотал"),
    SET_TUESDAY_ROUTE("Маршрут"),
    SET_MORNING_WEDNESDAY_KM("Утро"),
    SET_EVENING_WEDNESDAY_KM("Вечер"),
    SET_TOTAL_WEDNESDAY_KM("Тотал"),
    SET_WEDNESDAY_ROUTE("Маршрут"),
    SET_MORNING_THURSDAY_KM("Утро"),
    SET_EVENING_THURSDAY_KM("Вечер"),
    SET_TOTAL_THURSDAY_KM("Тотал"),
    SET_THURSDAY_ROUTE("Маршрут"),
    SET_MORNING_FRIDAY_KM("Утро"),
    SET_EVENING_FRIDAY_KM("Вечер"),
    SET_TOTAL_FRIDAY_KM("Тотал"),
    SET_FRIDAY_ROUTE("Маршрут"),
    CLEAR_ALL("Очистить"),
    ACCEPT_CLEAR("Да"),
    DECLINE_CLEAR("Нет"),
    MONDAY_DATE("Дата"),
    TUESDAY_DATE("Дата"),
    WEDNESDAY_DATE("Дата"),
    THURSDAY_DATE("Дата"),
    FRIDAY_DATE("Дата"),
    GENERAL("Основное"),
    DRIVER("Водитель"),
    DATA("Дата"),
    MODEL_AUTO("Марка авто"),
    NUMBER_AUTO("Гос.номер"),
    START_WEEK_PROBEG("Показания до раб.дня"),
    END_WEEK_PROBEG("Показания после раб.дня"),
    START_BALANCE_LITRES("Остаток литров 'Начало'"),
    END_BALANCE_LITRES("Остаток литров 'Конец'"),
    TOTAL_WEEK_KM("Пройдено за неделю"),
    FUEL_NORM("Норма расхода"),
    LITRES_SPEND("Расход литров"),
    FUELING("Заправлено литров"),

    BACK("Назад");

    private final String name;

    Buttons(String name) {
        this.name = name;
    }
}
