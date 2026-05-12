package com.orgtgbot.service;

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
    private static final int[] ROW_INDEXES = {10, 12, 14, 16, 18};

    private static final int KM_COLUMN = 9;

    public byte[] generateReport() throws Exception {
        List<ReportEntry> entries = probegService.getAll();

        ClassPathResource resource = new ClassPathResource("probeg_template.xlsx");

        try (InputStream is = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            for (ReportEntry entry : entries) {
                int rowIndex = ROW_INDEXES[entry.getRowNumber() - 1];
                Row row = sheet.getRow(rowIndex);
                if (row == null) row = sheet.createRow(rowIndex);

                Cell cell = row.getCell(KM_COLUMN);
                if (cell == null) cell = row.createCell(KM_COLUMN);
                cell.setCellValue(entry.getKilometers());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
