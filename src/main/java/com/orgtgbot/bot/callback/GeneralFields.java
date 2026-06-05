package com.orgtgbot.bot.callback;

import com.orgtgbot.service.bridge.AppContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Getter
public enum GeneralFields {
    NONE("Неизвестно", null, null, null, null),
    BACK("Назад", null, null, null, null),
    MAIN_MENU("Главное меню", null, null, null, null),

    PROBEG_MENU("Меню пробега", MAIN_MENU, MAIN_MENU, null, null),
    GENERAL("Основное", PROBEG_MENU, null, null, null),

    PROBEG_MONDAY("Понедельник", PROBEG_MENU, PROBEG_MENU, null, null),
    PROBEG_TUESDAY("Вторник", PROBEG_MENU, PROBEG_MENU, null, null),
    PROBEG_WEDNESDAY("Среда", PROBEG_MENU, PROBEG_MENU, null, null),
    PROBEG_THURSDAY("Четверг", PROBEG_MENU, PROBEG_MENU, null, null),
    PROBEG_FRIDAY("Пятница", PROBEG_MENU, PROBEG_MENU, null, null),

    GET_REPORT("Получить отчёт", PROBEG_MENU, PROBEG_MENU, null, null),
    CLEAR_ALL("Очистить", PROBEG_MENU, PROBEG_MENU, null, null),
    ACCEPT_CLEAR("Да", CLEAR_ALL, CLEAR_ALL, null, null),
    DECLINE_CLEAR("Нет", CLEAR_ALL, CLEAR_ALL, null, null),

    SET_MORNING_MONDAY_KM("Утро Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(1).getMorningKm()),
            (ctx, val) -> ctx.probegService().updateMorningKm(1, Integer.parseInt(val))),
    SET_EVENING_MONDAY_KM("Вечер Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(1).getEveningKm()),
            (ctx, val) -> ctx.probegService().updateEveningKm(1, Integer.parseInt(val))),
    SET_TOTAL_MONDAY_KM("Тотал Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(1).getTotalKm()),
            (ctx, val) -> ctx.probegService().updateFields(1, entry -> entry.setTotalKm(Integer.parseInt(val)))),
    SET_MONDAY_ROUTE("Маршрут Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> ctx.probegService().getReportEntry(1).getRoute(),
            (ctx, val) -> ctx.probegService().updateFields(1, entry -> entry.setRoute(val))),
    MONDAY_DATE("Понедельник Дата", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> ctx.dateService().getDatesEntry(1).getDate(),
            (ctx, val) -> ctx.dateService().setDate(1, val)),

    SET_MORNING_TUESDAY_KM("Утро Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(2).getMorningKm()),
            (ctx, val) -> ctx.probegService().updateMorningKm(2, Integer.parseInt(val))),
    SET_EVENING_TUESDAY_KM("Вечер Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(2).getEveningKm()),
            (ctx, val) -> ctx.probegService().updateEveningKm(2, Integer.parseInt(val))),
    SET_TOTAL_TUESDAY_KM("Тотал Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(2).getTotalKm()),
            (ctx, val) -> ctx.probegService().updateFields(2, entry -> entry.setTotalKm(Integer.parseInt(val)))),
    SET_TUESDAY_ROUTE("Маршрут Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> ctx.probegService().getReportEntry(2).getRoute(),
            (ctx, val) -> ctx.probegService().updateFields(2, entry -> entry.setRoute(val))),
    TUESDAY_DATE("Вторник Дата", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> ctx.dateService().getDatesEntry(2).getDate(),
            (ctx, val) -> ctx.dateService().setDate(2, val)),

    SET_MORNING_WEDNESDAY_KM("Утро Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(3).getMorningKm()),
            (ctx, val) -> ctx.probegService().updateMorningKm(3, Integer.parseInt(val))),
    SET_EVENING_WEDNESDAY_KM("Вечер Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(3).getEveningKm()),
            (ctx, val) -> ctx.probegService().updateEveningKm(3, Integer.parseInt(val))),
    SET_TOTAL_WEDNESDAY_KM("Тотал Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(3).getTotalKm()),
            (ctx, val) -> ctx.probegService().updateFields(3, entry -> entry.setTotalKm(Integer.parseInt(val)))),
    SET_WEDNESDAY_ROUTE("Маршрут Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> ctx.probegService().getReportEntry(3).getRoute(),
            (ctx, val) -> ctx.probegService()
                    .updateFields(3, entry -> entry.setRoute(val))),
    WEDNESDAY_DATE("Среда Дата", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> ctx.dateService().getDatesEntry(3).getDate(),
            (ctx, val) -> ctx.dateService().setDate(3, val)),

    SET_MORNING_THURSDAY_KM("Утро Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(4).getMorningKm()),
            (ctx, val) -> ctx.probegService().updateMorningKm(4, Integer.parseInt(val))),
    SET_EVENING_THURSDAY_KM("Вечер Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(4).getEveningKm()),
            (ctx, val) -> ctx.probegService().updateEveningKm(4, Integer.parseInt(val))),
    SET_TOTAL_THURSDAY_KM("Тотал Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(4).getTotalKm()),
            (ctx, val) -> ctx.probegService().updateFields(4, entry -> entry.setTotalKm(Integer.parseInt(val)))),
    SET_THURSDAY_ROUTE("Маршрут Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> ctx.probegService().getReportEntry(4).getRoute(),
            (ctx, val) -> ctx.probegService().updateFields(4, entry -> entry.setRoute(val))),
    THURSDAY_DATE("Четверг Дата", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> ctx.dateService().getDatesEntry(4).getDate(),
            (ctx, val) -> ctx.dateService().setDate(4, val)),

    SET_MORNING_FRIDAY_KM("Утро Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(5).getMorningKm()),
            (ctx, val) -> ctx.probegService().updateMorningKm(5, Integer.parseInt(val))),
    SET_EVENING_FRIDAY_KM("Вечер Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(5).getEveningKm()),
            (ctx, val) -> ctx.probegService().updateEveningKm(5, Integer.parseInt(val))),
    SET_TOTAL_FRIDAY_KM("Тотал Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(5).getTotalKm()),
            (ctx, val) -> ctx.probegService().updateFields(5, entry -> entry.setTotalKm(Integer.parseInt(val)))),
    SET_FRIDAY_ROUTE("Маршрут Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> ctx.probegService().getReportEntry(5).getRoute(),
            (ctx, val) -> ctx.probegService().updateFields(5, entry -> entry.setRoute(val))),
    FRIDAY_DATE("Пятница Дата", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> ctx.dateService().getDatesEntry(5).getDate(),
            (ctx, val) -> ctx.dateService().setDate(5, val)),

    DRIVER("Водитель", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleEntry().getName(),
            (ctx, val) -> ctx.generalService().getSingleEntry().setName(val)),
    DATE("Дата", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleEntry().getDate(),
            (ctx, val) -> ctx.generalService().getSingleEntry().setDate(val)),
    MODEL_AUTO("Марка авто", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleEntry().getCarModel(),
            (ctx, val) -> ctx.generalService().getSingleEntry().setCarModel(val)),
    NUMBER_AUTO("Гос.номер", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleEntry().getCarNumber(),
            (ctx, val) -> ctx.generalService().getSingleEntry().setCarNumber(val)),
    START_WEEK_PROBEG("Показания до раб.дня", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry().getStartWeekProbeg()),
            (ctx, val) -> ctx.generalService().getSingleEntry().setStartWeekProbeg(Integer.parseInt(val))),
    END_WEEK_PROBEG("Показания после раб.дня", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry().getEndWeekProbeg()),
            (ctx, val) -> ctx.generalService().getSingleEntry().setEndWeekProbeg(Integer.parseInt(val))),
    START_BALANCE_LITRES("Остаток литров 'Начало'", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry().getStartBalanceLitres()),
            (ctx, val) -> ctx.generalService().getSingleEntry().setStartBalanceLitres(new BigDecimal(val.replace(",", ".")))),
    END_BALANCE_LITRES("Остаток литров 'Конец'", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry().getEndBalanceLitres()),
            (ctx, val) -> ctx.generalService().getSingleEntry().setEndBalanceLitres(new BigDecimal(val.replace(",", ".")))),
    TOTAL_WEEK_KM("Пройдено за неделю", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry().getTotalWeekKm()),
            (ctx, val) -> ctx.generalService().getSingleEntry().setTotalWeekKm(Integer.parseInt(val))),
    FUEL_NORM("Норма расхода", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry().getFuelNorm()),
            (ctx, val) -> ctx.generalService().getSingleEntry().setFuelNorm(new BigDecimal(val.replace(",", ".")))),
    LITRES_SPEND("Расход литров", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry().getLitresSpend()),
            (ctx, val) -> ctx.generalService().getSingleEntry().setLitresSpend(new BigDecimal(val.replace(",", ".")))),
    FUELING("Заправлено литров", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry().getFueling()),
            (ctx, val) -> ctx.generalService().getSingleEntry().setFueling(Integer.parseInt(val)));

    private final String description;
    private final GeneralFields parent;
    private final GeneralFields group;
    private final Function<AppContext, String> getter;
    private final BiConsumer<AppContext, String> setter;

    public String getValue(AppContext ctx) {
        return getter != null ? getter.apply(ctx) : "";
    }

    public void setValue(AppContext ctx, String value) {
        if (setter != null) setter.accept(ctx, value);
    }
}
