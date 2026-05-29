package com.orgtgbot.bot.keyboard;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum Buttons {
    MAIN_MENU("Главное меню", null),
    PROBEG_MENU("Меню пробега", null),
    PROBEG_MONDAY("Понедельник", null),
    PROBEG_TUESDAY("Вторник", null),
    PROBEG_WEDNESDAY("Среда", null),
    PROBEG_THURSDAY("Четверг", null),
    PROBEG_FRIDAY("Пятница", null),
    GET_REPORT("Получить отчёт", null),
    SET_MORNING_MONDAY_KM("Утро", 1),
    SET_EVENING_MONDAY_KM("Вечер", 1),
    SET_TOTAL_MONDAY_KM("Тотал", 1),
    SET_MONDAY_ROUTE("Маршрут", 1),
    SET_MORNING_TUESDAY_KM("Утро", 2),
    SET_EVENING_TUESDAY_KM("Вечер", 2),
    SET_TOTAL_TUESDAY_KM("Тотал", 2),
    SET_TUESDAY_ROUTE("Маршрут", 2),
    SET_MORNING_WEDNESDAY_KM("Утро", 3),
    SET_EVENING_WEDNESDAY_KM("Вечер", 3),
    SET_TOTAL_WEDNESDAY_KM("Тотал", 3),
    SET_WEDNESDAY_ROUTE("Маршрут", 3),
    SET_MORNING_THURSDAY_KM("Утро", 4),
    SET_EVENING_THURSDAY_KM("Вечер", 4),
    SET_TOTAL_THURSDAY_KM("Тотал", 4),
    SET_THURSDAY_ROUTE("Маршрут", 4),
    SET_MORNING_FRIDAY_KM("Утро", 5),
    SET_EVENING_FRIDAY_KM("Вечер", 5),
    SET_TOTAL_FRIDAY_KM("Тотал", 5),
    SET_FRIDAY_ROUTE("Маршрут", 5),
    CLEAR_ALL("Очистить", null),
    ACCEPT_CLEAR("Да", null),
    DECLINE_CLEAR("Нет", null),
    MONDAY_DATE("Дата", null),
    TUESDAY_DATE("Дата", null),
    WEDNESDAY_DATE("Дата", null),
    THURSDAY_DATE("Дата", null),
    FRIDAY_DATE("Дата", null),
    GENERAL("Основное", null),
    DRIVER("Водитель", null),
    DATA("Дата", null),
    MODEL_AUTO("Марка авто", null),
    NUMBER_AUTO("Гос.номер", null),
    START_WEEK_PROBEG("Показания до раб.дня", null),
    END_WEEK_PROBEG("Показания после раб.дня", null),
    START_BALANCE_LITRES("Остаток литров 'Начало'", null),
    END_BALANCE_LITRES("Остаток литров 'Конец'", null),
    TOTAL_WEEK_KM("Пройдено за неделю", null),
    FUEL_NORM("Норма расхода", null),
    LITRES_SPEND("Расход литров", null),
    FUELING("Заправлено литров", null),

    BACK("Назад", null);

    private final String name;
    private final Integer dayNumber;

    Buttons(String name, Integer dayNumber) {
        this.name = name;
        this.dayNumber = dayNumber;
    }

    public Optional<Integer> getDayNumber() {
        return Optional.ofNullable(dayNumber);
    }
}
