package com.orgtgbot.service;

import com.orgtgbot.entity.DatesEntry;
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

    private static final int[] ROW_KM = {11, 12, 13, 14, 15}; // ROW INDEXES
    private static final int KM_COLUMN = 9; // J
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

                if (entry.getTotalKm() != 0)
                    kmCell.setCellValue(entry.getTotalKm()); // writes the value
                if (!entry.getRoute().trim().isEmpty())
                    routeCell.setCellValue(entry.getRoute());
                if (!dataModule[entry.getDayNumber() - 1].getDate().trim().isEmpty())
                    dateCell.setCellValue(dataModule[entry.getDayNumber() - 1].getDate());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
