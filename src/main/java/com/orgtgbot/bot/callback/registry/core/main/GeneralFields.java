package com.orgtgbot.bot.callback.registry.core.main;

import com.orgtgbot.dto.DatesEntryDto;
import com.orgtgbot.dto.GeneralEntryDto;
import com.orgtgbot.dto.ReportEntryDto;
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
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),1)
                    .morningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),1,
                            ReportEntryDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_MONDAY_KM("Вечер Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),1)
                    .eveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),1,
                            ReportEntryDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_MONDAY_KM("Тотал Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),1)
                    .totalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),1,
                            ReportEntryDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_MONDAY_ROUTE("Маршрут Понедельник", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> ctx.probegService().getReportEntryDto(ctx.chatId(),1)
                    .route(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),1,
                            ReportEntryDto.builder().route(val).build())),
    MONDAY_DATE("Понедельник Дата", PROBEG_MONDAY, PROBEG_MONDAY,
            ctx -> ctx.dateService().getDatesDto(ctx.chatId(),1)
                    .date(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),1,
                            DatesEntryDto.builder().date(val).build())),

    SET_MORNING_TUESDAY_KM("Утро Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),2)
                    .morningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),2,
                            ReportEntryDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_TUESDAY_KM("Вечер Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),2)
                    .eveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),2,
                            ReportEntryDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_TUESDAY_KM("Тотал Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),2)
                    .totalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),2,
                            ReportEntryDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_TUESDAY_ROUTE("Маршрут Вторник", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> ctx.probegService().getReportEntryDto(ctx.chatId(),2)
                    .route(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),2,
                            ReportEntryDto.builder().route(val).build())),
    TUESDAY_DATE("Вторник Дата", PROBEG_TUESDAY, PROBEG_TUESDAY,
            ctx -> ctx.dateService().getDatesDto(ctx.chatId(),2)
                    .date(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),2,
                            DatesEntryDto.builder().date(val).build())),

    SET_MORNING_WEDNESDAY_KM("Утро Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),3)
                    .morningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),3,
                            ReportEntryDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_WEDNESDAY_KM("Вечер Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),3)
                    .eveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),3,
                            ReportEntryDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_WEDNESDAY_KM("Тотал Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),3)
                    .totalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),3,
                            ReportEntryDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_WEDNESDAY_ROUTE("Маршрут Среда", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> ctx.probegService().getReportEntryDto(ctx.chatId(),3)
                    .route(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),3,
                            ReportEntryDto.builder().route(val).build())),
    WEDNESDAY_DATE("Среда Дата", PROBEG_WEDNESDAY, PROBEG_WEDNESDAY,
            ctx -> ctx.dateService().getDatesDto(ctx.chatId(),3)
                    .date(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),3,
                            DatesEntryDto.builder().date(val).build())),

    SET_MORNING_THURSDAY_KM("Утро Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),4)
                    .morningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),4,
                            ReportEntryDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_THURSDAY_KM("Вечер Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),4)
                    .eveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),4,
                            ReportEntryDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_THURSDAY_KM("Тотал Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),4)
                    .totalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),4,
                            ReportEntryDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_THURSDAY_ROUTE("Маршрут Четверг", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> ctx.probegService().getReportEntryDto(ctx.chatId(),4)
                    .route(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),4,
                            ReportEntryDto.builder().route(val).build())),
    THURSDAY_DATE("Четверг Дата", PROBEG_THURSDAY, PROBEG_THURSDAY,
            ctx -> ctx.dateService().getDatesDto(ctx.chatId(),4)
                    .date(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),4,
                            DatesEntryDto.builder().date(val).build())),

    SET_MORNING_FRIDAY_KM("Утро Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),5)
                    .morningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),5,
                            ReportEntryDto.builder().morningKm(Integer.parseInt(val)).build())),
    SET_EVENING_FRIDAY_KM("Вечер Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),5)
                    .eveningKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),5,
                            ReportEntryDto.builder().eveningKm(Integer.parseInt(val)).build())),
    SET_TOTAL_FRIDAY_KM("Тотал Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> String.valueOf(ctx.probegService().getReportEntryDto(ctx.chatId(),5)
                    .totalKm()),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),5,
                            ReportEntryDto.builder().totalKm(Integer.parseInt(val)).build())),
    SET_FRIDAY_ROUTE("Маршрут Пятница", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> ctx.probegService().getReportEntryDto(ctx.chatId(),5)
                    .route(),
            (ctx, val) -> ctx.probegService()
                    .updateProbegInfo(ctx.chatId(),5,
                            ReportEntryDto.builder().route(val).build())),
    FRIDAY_DATE("Пятница Дата", PROBEG_FRIDAY, PROBEG_FRIDAY,
            ctx -> ctx.dateService().getDatesDto(ctx.chatId(),5)
                    .date(),
            (ctx, val) -> ctx.dateService()
                    .setDate(ctx.chatId(),5,
                            DatesEntryDto.builder().date(val).build())),

    DRIVER("Водитель", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleDto(ctx.chatId()).name(),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().name(val).build())),
    DATE("Дата", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleDto(ctx.chatId()).date(),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().date(val).build())),
    MODEL_AUTO("Марка авто", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleDto(ctx.chatId()).carModel(),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().carModel(val).build())),
    NUMBER_AUTO("Гос.номер", GENERAL, GENERAL,
            ctx -> ctx.generalService().getSingleDto(ctx.chatId()).carNumber(),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().carNumber(val).build())),
    START_WEEK_PROBEG("Показания до раб.дня", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleDto(ctx.chatId()).startWeekProbeg()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().startWeekProbeg(Integer.parseInt(val)).build())),
    END_WEEK_PROBEG("Показания после раб.дня", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleDto(ctx.chatId()).endWeekProbeg()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().endWeekProbeg(Integer.parseInt(val)).build())),
    START_BALANCE_LITRES("Остаток литров 'Начало'", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleDto(ctx.chatId()).startBalanceLitres()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().startBalanceLitres(new BigDecimal(val)).build())),
    END_BALANCE_LITRES("Остаток литров 'Конец'", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleDto(ctx.chatId()).endBalanceLitres()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().endBalanceLitres(new BigDecimal(val)).build())),
    TOTAL_WEEK_KM("Пройдено за неделю", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleDto(ctx.chatId()).totalWeekKm()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().totalWeekKm(Integer.parseInt(val)).build())),
    FUEL_NORM("Норма расхода", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleDto(ctx.chatId()).fuelNorm()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().fuelNorm(new BigDecimal(val)).build())),
    LITRES_SPEND("Расход литров", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleDto(ctx.chatId()).litresSpend()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().litresSpend(new BigDecimal(val)).build())),
    FUELING("Заправлено литров", GENERAL, GENERAL,
            ctx -> String.valueOf(ctx.generalService().getSingleDto(ctx.chatId()).fueling()),
            (ctx, val) -> ctx.generalService().updateGeneralInfo(ctx.chatId(),
                    GeneralEntryDto.builder().fueling(Integer.parseInt(val)).build())),

    MAIN_REMINDER("Напоминалка", MAIN_MENU, MAIN_MENU,
            ctx -> ctx.reminderService().getAllRemindersFormatted(ctx.chatId()),
            (ctx, val) -> ctx.reminderService().addRemind(ctx.chatId(), val)),

    MAIN_RECEIPT("Бухгалтер", MAIN_MENU, MAIN_MENU,
            ctx -> ctx.bookkeeperService().getWhatWasSpendDuringTheMonth(ctx.chatId()),
            (ctx, val) -> ctx.bookkeeperService().addReceipt(ctx.chatId(), val)),
    ALL_RECEIPTS("История", MAIN_RECEIPT, MAIN_RECEIPT,
            ctx -> ctx.bookkeeperService().getAllHistory(ctx.chatId(), ctx.page()),
            null),
    ALL_PREVIOUS_PAGE("Предыдущая", ALL_RECEIPTS, null,
            ctx -> ctx.bookkeeperService().getAllHistory(ctx.chatId(), ctx.page()),
            null),
    ALL_NEXT_PAGE("Следующая", ALL_RECEIPTS, null,
            ctx -> ctx.bookkeeperService().getAllHistory(ctx.chatId(), ctx.page()),
            null);

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
