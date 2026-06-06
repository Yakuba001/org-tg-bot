package com.orgtgbot.service.services;

import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.entity.GeneralEntry;
import com.orgtgbot.entity.ReportEntry;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final ProbegService probegService;
    private final DateService dateService;
    private final GeneralService generalService;

    private static final int[] ROW_KM = {11, 12, 13, 14, 15}; // Индексы строк для Пн-Пт
    private static final int[] GENERAL_ROW = {3, 5, 7, 21, 23}; // Строки для общих данных

    // Индексы колонок (соответствуют буквам в Excel)
    private static final int START_BALANCE_LITRES_COLUMN = 11; // L
    private static final int DATE_GENERAL_COLUMN = 10;        // K
    private static final int KM_COLUMN = 9;                   // J
    private static final int FUEL_NORM_COLUMN = 8;            // I
    private static final int TOTAL_START_COLUMN = 7;           // H
    private static final int TOTAL_COLUMN = 6;                 // G
    private static final int TOTAL_END_COLUMN = 5;               // F
    private static final int DRIVER_COLUMN = 4;                // E
    private static final int ROUTE_COLUMN = 3;                 // D
    private static final int DATE_COLUMN = 2;                  // C

    @Transactional(readOnly = true)
    public byte[] generateReport(Long chatId) throws Exception {
        // 1. Извлекаем изолированные данные конкретного водителя
        List<ReportEntry> entries = probegService.getAll(chatId);
        List<DatesEntry> dates = dateService.getAll(chatId);
        GeneralEntry general = generalService.getSingleEntry(chatId);

        DatesEntry[] dataModule = {dates.get(0), dates.get(1), dates.get(2), dates.get(3), dates.get(4)};

        ClassPathResource resource = new ClassPathResource("probeg.xlsx");

        try (InputStream is = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            // 2. Заполняем ежедневные данные (Пробег, Маршруты, Даты дней)
            for (ReportEntry entry : entries) {
                int rowIndex = ROW_KM[entry.getDayNumber() - 1];
                Row row = sheet.getRow(rowIndex);
                if (row == null) row = sheet.createRow(rowIndex);

                Cell kmCell = getOrCreateCell(row, KM_COLUMN);
                Cell routeCell = getOrCreateCell(row, ROUTE_COLUMN);
                Cell dateCell = getOrCreateCell(row, DATE_COLUMN);

                if (entry.getTotalKm() != 0) {
                    kmCell.setCellValue(entry.getTotalKm());
                }
                if (entry.getRoute() != null && !entry.getRoute().trim().isEmpty()) {
                    routeCell.setCellValue(entry.getRoute());
                }

                String dayDate = dataModule[entry.getDayNumber() - 1].getDate();
                if (dayDate != null && !dayDate.trim().isEmpty()) {
                    dateCell.setCellValue(dayDate);
                }
            }

            // Строка 3: Водитель, Марка авто, Гос.номер
            Row row3 = getOrCreateRow(sheet, GENERAL_ROW[0]);
            getOrCreateCell(row3, DRIVER_COLUMN).setCellValue(general.getName());
            getOrCreateCell(row3, TOTAL_END_COLUMN).setCellValue(general.getCarModel());
            getOrCreateCell(row3, TOTAL_COLUMN).setCellValue(general.getCarNumber());

            // Строка 5: Дата отчета (в колонку K)
            Row row5 = getOrCreateRow(sheet, GENERAL_ROW[1]);
            if (general.getDate() != null) {
                getOrCreateCell(row5, DATE_GENERAL_COLUMN).setCellValue(general.getDate());
            }

            // Строка 7: Показания спидометра (До / После рабочего дня)
            Row row7 = getOrCreateRow(sheet, GENERAL_ROW[2]);
            getOrCreateCell(row7, TOTAL_START_COLUMN).setCellValue(general.getStartWeekProbeg());
            getOrCreateCell(row7, TOTAL_END_COLUMN).setCellValue(general.getEndWeekProbeg());

            // Строка 21: Литры (Остаток литров Начало / Конец, Всего пройдено)
            Row row21 = getOrCreateRow(sheet, GENERAL_ROW[3]);
            if (general.getStartBalanceLitres() != null) {
                getOrCreateCell(row21, START_BALANCE_LITRES_COLUMN).setCellValue(general.getStartBalanceLitres().doubleValue());
            }
            if (general.getEndBalanceLitres() != null) {
                getOrCreateCell(row21, TOTAL_START_COLUMN).setCellValue(general.getEndBalanceLitres().doubleValue());
            }
            getOrCreateCell(row21, TOTAL_END_COLUMN).setCellValue(general.getTotalWeekKm());

            // Строка 23: Расход топлива (Норма расхода, Расход литров, Заправлено литров)
            Row row23 = getOrCreateRow(sheet, GENERAL_ROW[4]);
            if (general.getFuelNorm() != null) {
                getOrCreateCell(row23, FUEL_NORM_COLUMN).setCellValue(general.getFuelNorm().doubleValue());
            }
            if (general.getLitresSpend() != null) {
                getOrCreateCell(row23, TOTAL_START_COLUMN).setCellValue(general.getLitresSpend().doubleValue());
            }
            getOrCreateCell(row23, TOTAL_END_COLUMN).setCellValue(general.getFueling());

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private Row getOrCreateRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        return row != null ? row : sheet.createRow(rowIndex);
    }

    private Cell getOrCreateCell(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        return cell != null ? cell : row.createCell(columnIndex);
    }
}
