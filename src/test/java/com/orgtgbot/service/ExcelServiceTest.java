package com.orgtgbot.service;

import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.entity.GeneralEntry;
import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.service.services.DateService;
import com.orgtgbot.service.services.ExcelService;
import com.orgtgbot.service.services.GeneralService;
import com.orgtgbot.service.services.ProbegService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExcelServiceTest {

    @Mock
    private ProbegService probegService;
    @Mock
    private DateService dateService;
    @Mock
    private GeneralService generalService;

    @InjectMocks
    private ExcelService excelService;

    @Test
    void generateReport_shouldCorrectlyFillExcel() throws Exception {
        Long chatId = 999L;
        List<DatesEntry> mockDates = List.of(
                DatesEntry.builder().date("01.06").build(),
                DatesEntry.builder().date("02.06").build(),
                DatesEntry.builder().date("03.06").build(),
                DatesEntry.builder().date("04.06").build(),
                DatesEntry.builder().date("05.06").build()
        );
        List<ReportEntry> mockReports = List.of(
                ReportEntry.builder().dayNumber(1).morningKm(10).eveningKm(20).totalKm(10).route("Monday").build()
        );
        GeneralEntry mockGeneral = GeneralEntry.builder()
                .name("Boss")
                .carModel("Car")
                .carNumber("123")
                .date("01.06.01")
                .startWeekProbeg(10000)
                .endWeekProbeg(10500)
                .startBalanceLitres(BigDecimal.valueOf(50.5))
                .endBalanceLitres(BigDecimal.valueOf(10.2))
                .totalWeekKm(500)
                .fuelNorm(BigDecimal.valueOf(8.5))
                .litresSpend(BigDecimal.valueOf(42.5))
                .fueling(40)
                .build();
        when(dateService.getAll(chatId)).thenReturn(mockDates);
        when(probegService.getAll(chatId)).thenReturn(mockReports);
        when(generalService.getSingleEntry(chatId)).thenReturn(mockGeneral);

        byte[] excelBytes = excelService.generateReport(chatId);

        assertThat(excelBytes).isNotEmpty();

        try (InputStream is = new ByteArrayInputStream(excelBytes); Workbook workbook = new XSSFWorkbook(is)){
            Sheet sheet = workbook.getSheetAt(0);
            assertThat(sheet).isNotNull();

            Row rowMonday = sheet.getRow(11);
            assertThat(rowMonday).isNotNull();
            assertThat(rowMonday.getCell(9).getNumericCellValue()).isEqualTo(10);
            assertThat(rowMonday.getCell(3).getStringCellValue()).isEqualTo("Monday");
            assertThat(rowMonday.getCell(2).getStringCellValue()).isEqualTo("01.06");

            Row row3 = sheet.getRow(3);
            assertThat(row3).isNotNull();
            assertThat(row3.getCell(4).getStringCellValue()).isEqualTo("Boss");
            assertThat(row3.getCell(5).getStringCellValue()).isEqualTo("Car");
            assertThat(row3.getCell(6).getStringCellValue()).isEqualTo("123");

            Row row5 = sheet.getRow(5);
            assertThat(row5.getCell(10).getStringCellValue()).isEqualTo("01.06.01");

            Row row7 = sheet.getRow(7);
            assertThat(row7.getCell(7).getNumericCellValue()).isEqualTo(10000);
            assertThat(row7.getCell(5).getNumericCellValue()).isEqualTo(10500);

            Row row21 = sheet.getRow(21);
            assertThat(row21.getCell(11).getNumericCellValue()).isEqualTo(50.5);
            assertThat(row21.getCell(7).getNumericCellValue()).isEqualTo(10.2);
            assertThat(row21.getCell(5).getNumericCellValue()).isEqualTo(500);
        }
    }
}
