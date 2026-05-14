package com.orgtgbot.service;

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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final ProbegService probegService;
    private final DateService dateService;
    private final GeneralService generalService;

    private static final int[] ROW_KM = {11, 12, 13, 14, 15}; // ROW INDEXES
    private static final int[] GENERAL_ROW = {3, 5, 7, 21, 23};

    private static final int START_BALANCE_LITRES_COLUMN = 11; // L
    private static final int DATE_GENERAL_COLUMN = 10; // K
    private static final int KM_COLUMN = 9; // J
    private static final int FUEL_NORM_COLUMN = 8; // I
    private static final int TOTAL_START_COLUMN = 7; // H
    private static final int TOTAL_COLUMN = 6; // G
    private static final int TOTAL_END_COLUMN = 5; // F
    private static final int DRIVER_COLUMN = 4; // E
    private static final int ROUTE_COLUMN = 3; // D
    private static final int DATE_COLUMN = 2; // C

    public byte[] generateReport() throws Exception {
        List<ReportEntry> entries = probegService.getAll();
        List<DatesEntry> dates = dateService.getAll();
        DatesEntry[] dataModule = {dates.getFirst(), dates.get(1), dates.get(2), dates.get(3), dates.get(4)};

        ClassPathResource resource = new ClassPathResource("probeg_template.xlsx");

        try (InputStream is = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            for (ReportEntry entry : entries) {
                int rowIndex = ROW_KM[entry.getDayNumber() - 1];
                Row row = sheet.getRow(rowIndex);
                Cell kmCell = row.getCell(KM_COLUMN);
                Cell routeCell = row.getCell(ROUTE_COLUMN);
                Cell dateCell = row.getCell(DATE_COLUMN);
                Cell driverCell = row.getCell(DATE_GENERAL_COLUMN);

                if (entry.getTotalKm() != 0)
                    kmCell.setCellValue(entry.getTotalKm()); // writes the value
                if (!entry.getRoute().trim().isEmpty())
                    routeCell.setCellValue(entry.getRoute());
                if (!dataModule[entry.getDayNumber() - 1].getDate().trim().isEmpty())
                    dateCell.setCellValue(dataModule[entry.getDayNumber() - 1].getDate());
                driverCell.setCellValue(generalService.getAll().getFirst().getName());
            }

            addGeneralToDocument(sheet);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void addGeneralToDocument(Sheet sheet) {
        List<GeneralEntry> general = generalService.getAll();
        for (int i = 0; i < GENERAL_ROW.length; i++) {
            Row row = sheet.getRow(GENERAL_ROW[i]);
            Cell driver = row.getCell(DRIVER_COLUMN);
            Cell date = row.getCell(DATE_GENERAL_COLUMN);
            Cell modelAuto = row.getCell(DRIVER_COLUMN);
            Cell numberAuto = row.getCell(DRIVER_COLUMN);
            Cell totalWeekStart = row.getCell(TOTAL_START_COLUMN);
            Cell startBalanceLitres = row.getCell(START_BALANCE_LITRES_COLUMN);
            Cell totalKmForWeek = row.getCell(TOTAL_COLUMN);
            Cell fuelNorm = row.getCell(FUEL_NORM_COLUMN);
            Cell fuelSpend = row.getCell(DATE_GENERAL_COLUMN);
            Cell fuelLitres = row.getCell(START_BALANCE_LITRES_COLUMN);
            Cell totalWeekEnd = row.getCell(TOTAL_END_COLUMN);
            Cell endBalanceLitres = row.getCell(START_BALANCE_LITRES_COLUMN);

            if (i == 0) {
                driver.setCellValue(general.getFirst().getName());
                date.setCellValue(general.getFirst().getDate());
            }
            if (i == 1) {
                modelAuto.setCellValue(general.getFirst().getCarModel());
                numberAuto.setCellValue(general.getFirst().getCarNumber());
            }
            if (i == 2) {
                totalWeekStart.setCellValue(general.getFirst().getStartWeekProbeg());
                startBalanceLitres.setCellValue(String.valueOf(general.getFirst().getStartBalanceLitres()));
            }
            if (i == 3) {
                totalKmForWeek.setCellValue(general.getFirst().getTotalWeekKm());
                fuelNorm.setCellValue(String.valueOf(general.getFirst().getFuelNorm()));
                fuelSpend.setCellValue(String.valueOf(general.getFirst().getLitresSpend()));
                fuelLitres.setCellValue(String.valueOf(general.getFirst().getFueling()));
            }
            if (i == 4) {
                totalWeekEnd.setCellValue(general.getFirst().getEndWeekProbeg());
                endBalanceLitres.setCellValue(String.valueOf(general.getFirst().getEndBalanceLitres()));
            }
        }
    }
}
