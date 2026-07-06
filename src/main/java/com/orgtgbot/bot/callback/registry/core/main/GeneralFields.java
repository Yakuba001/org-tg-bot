package com.orgtgbot.bot.callback.registry.core.main;

import com.orgtgbot.dto.DatesUpdateDto;
import com.orgtgbot.dto.GeneralUpdateDto;
import com.orgtgbot.dto.ProbegUpdateDto;
import com.orgtgbot.service.bridge.AppContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Getter
public enum GeneralFields {
    NONE("Неизвестно", null, null, null, null),
    BACK("Назад", null, null, null, null),
    MAIN_MENU("Главное меню", null, null, null, null),

    PROBEG_MENU("Меню пробега", MAIN_MENU, MAIN_MENU, null, null),
    GENERAL("Основное", PROBEG_MENU, PROBEG_MENU, null, null),

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
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),1)
                    .getMorningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),1,
                            ProbegUpdateDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_MONDAY_KM("Вечер Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),1)
                    .getEveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),1,
                            ProbegUpdateDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_MONDAY_KM("Тотал Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),1)
                    .getTotalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),1,
                            ProbegUpdateDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_MONDAY_ROUTE("Маршрут Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> ctx.probegService().getReportEntry(ctx.chatId(),1)
                    .getRoute(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),1,
                            ProbegUpdateDto.builder().route(val).build())),
    MONDAY_DATE("Понедельник Дата", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> ctx.dateService().getDatesEntry(ctx.chatId(),1)
                    .getDate(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),1,
                            DatesUpdateDto.builder().date(val).build())),

    SET_MORNING_TUESDAY_KM("Утро Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),2)
                    .getMorningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),2,
                            ProbegUpdateDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_TUESDAY_KM("Вечер Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),2)
                    .getEveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),2,
                            ProbegUpdateDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_TUESDAY_KM("Тотал Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),2)
                    .getTotalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),2,
                            ProbegUpdateDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_TUESDAY_ROUTE("Маршрут Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> ctx.probegService().getReportEntry(ctx.chatId(),2)
                    .getRoute(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),2,
                            ProbegUpdateDto.builder().route(val).build())),
    TUESDAY_DATE("Вторник Дата", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> ctx.dateService().getDatesEntry(ctx.chatId(),2)
                    .getDate(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),2,
                            DatesUpdateDto.builder().date(val).build())),

    SET_MORNING_WEDNESDAY_KM("Утро Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),3)
                    .getMorningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),3,
                            ProbegUpdateDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_WEDNESDAY_KM("Вечер Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),3)
                    .getEveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),3,
                            ProbegUpdateDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_WEDNESDAY_KM("Тотал Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),3)
                    .getTotalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),3,
                            ProbegUpdateDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_WEDNESDAY_ROUTE("Маршрут Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> ctx.probegService().getReportEntry(ctx.chatId(),3)
                    .getRoute(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),3,
                            ProbegUpdateDto.builder().route(val).build())),
    WEDNESDAY_DATE("Среда Дата", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> ctx.dateService().getDatesEntry(ctx.chatId(),3)
                    .getDate(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),3,
                            DatesUpdateDto.builder().date(val).build())),

    SET_MORNING_THURSDAY_KM("Утро Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),4)
                    .getMorningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),4,
                            ProbegUpdateDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_THURSDAY_KM("Вечер Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),4)
                    .getEveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),4,
                            ProbegUpdateDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_THURSDAY_KM("Тотал Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),4)
                    .getTotalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),4,
                            ProbegUpdateDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_THURSDAY_ROUTE("Маршрут Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> ctx.probegService().getReportEntry(ctx.chatId(),4)
                    .getRoute(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),4,
                            ProbegUpdateDto.builder().route(val).build())),
    THURSDAY_DATE("Четверг Дата", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> ctx.dateService().getDatesEntry(ctx.chatId(),4)
                    .getDate(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),4,
                            DatesUpdateDto.builder().date(val).build())),

    SET_MORNING_FRIDAY_KM("Утро Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),5)
                    .getMorningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),5,
                            ProbegUpdateDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_FRIDAY_KM("Вечер Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),5)
                    .getEveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),5,
                            ProbegUpdateDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_FRIDAY_KM("Тотал Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntry(ctx.chatId(),5)
                    .getTotalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),5,
                            ProbegUpdateDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_FRIDAY_ROUTE("Маршрут Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> ctx.probegService().getReportEntry(ctx.chatId(),5)
                    .getRoute(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),5,
                            ProbegUpdateDto.builder().route(val).build())),
    FRIDAY_DATE("Пятница Дата", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> ctx.dateService().getDatesEntry(ctx.chatId(),5)
                    .getDate(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),5,
                            DatesUpdateDto.builder().date(val).build())),

    DRIVER("Водитель", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleEntry(ctx.chatId()).getName(),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().name(val).build())),
    DATE("Дата", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleEntry(ctx.chatId()).getDate(),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().date(val).build())),
    MODEL_AUTO("Марка авто", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleEntry(ctx.chatId()).getCarModel(),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().carModel(val).build())),
    NUMBER_AUTO("Гос.номер", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleEntry(ctx.chatId()).getCarNumber(),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().carNumber(val).build())),
    START_WEEK_PROBEG("Показания до раб.дня", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry(ctx.chatId()).getStartWeekProbeg()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().startWeekProbeg(Integer.parseInt(val)).build())),
    END_WEEK_PROBEG("Показания после раб.дня", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry(ctx.chatId()).getEndWeekProbeg()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().endWeekProbeg(Integer.parseInt(val)).build())),
    START_BALANCE_LITRES("Остаток литров 'Начало'", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry(ctx.chatId()).getStartBalanceLitres()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().startBalanceLitres(val).build())),
    END_BALANCE_LITRES("Остаток литров 'Конец'", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry(ctx.chatId()).getEndBalanceLitres()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().endBalanceLitres(val).build())),
    TOTAL_WEEK_KM("Пройдено за неделю", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry(ctx.chatId()).getTotalWeekKm()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().totalWeekKm(Integer.parseInt(val)).build())),
    FUEL_NORM("Норма расхода", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry(ctx.chatId()).getFuelNorm()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().fuelNorm(val).build())),
    LITRES_SPEND("Расход литров", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry(ctx.chatId()).getLitresSpend()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().litresSpend(val).build())),
    FUELING("Заправлено литров", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleEntry(ctx.chatId()).getFueling()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralUpdateDto.builder().fueling(Integer.parseInt(val)).build())),

    MAIN_REMINDER("Напоминалка", MAIN_MENU, MAIN_MENU,
            ctx -> ctx.reminderService().getAllRemindersFormatted(ctx.chatId()),
            (ctx, val) -> ctx.reminderService().addRemind(ctx.chatId(), val)),

    MAIN_RECEIPT("Бухгалтер", MAIN_MENU, MAIN_MENU,
            ctx -> ctx.bookkeeperService().getWhatWasSpendDuringTheMonth(ctx.chatId()),
            (ctx, val) -> ctx.bookkeeperService().addReceipt(ctx.chatId(), val));

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
