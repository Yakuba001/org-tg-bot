package com.orgtgbot.unit.service;

import com.orgtgbot.dto.bookkeeper.ReceiptItemDto;
import com.orgtgbot.entity.bookkeeper.BookkeeperRecord;
import com.orgtgbot.mapper.bookkeeper.BookkeeperMapper;
import com.orgtgbot.repository.BookkeeperRepository;
import com.orgtgbot.service.filehandler.image.ImageChecker;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookkeeperServiceTest {

    @Mock private BookkeeperRepository bookkeeperRepository;
    @Mock private GeminiParserService geminiParserService;
    @Mock private ImageChecker imageChecker;

    @Spy
    private BookkeeperMapper bookkeeperMapper = Mappers.getMapper(BookkeeperMapper.class);

    private BookkeeperService bookkeeperService;

    private static final long CHAT_ID = 999L;

    @BeforeEach
    void setUp() {
        bookkeeperService =
                new BookkeeperService(bookkeeperRepository, geminiParserService, imageChecker, bookkeeperMapper);
    }

    @Test
    void addReceipt_ShouldSaveReceiptToDb() {
        byte[] testBytes = new byte[]{1, 2, 3};
        String testMimeType = "image/jpeg";
        String testFileId = "file_0.jpg";
        List<ReceiptItemDto> receiptItemDtoList = List.of(ReceiptItemDto.builder()
                .item("Milk")
                .price(new BigDecimal("122.4")).build());
        LocalDate today = LocalDate.now();
        when(imageChecker.getMimeType(testFileId)).thenReturn(testMimeType);
        when(imageChecker.downloadImageBytes(testFileId)).thenReturn(testBytes);
        when(geminiParserService.parseReceiptImage(testBytes, testMimeType)).thenReturn(receiptItemDtoList);

        bookkeeperService.addReceipt(CHAT_ID, testFileId);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<BookkeeperRecord>> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(bookkeeperRepository, times(1)).saveAll(listCaptor.capture());
        List<BookkeeperRecord> savedRecords = listCaptor.getValue();
        assertNotNull(savedRecords);
        assertEquals(1, savedRecords.size());

        BookkeeperRecord savedRecord = savedRecords.getFirst();
        assertEquals(CHAT_ID, savedRecord.getTelegramChatId());
        assertEquals("Milk", savedRecord.getItemName());
        assertEquals(new BigDecimal("122.4"), savedRecord.getPrice());
        assertEquals(today, savedRecord.getPurchaseDate());
    }

    @Test
    void getWhatWasSpendDuringTheMonth_ShouldReturnStringWithCorrectTotal() {
        BigDecimal total = new BigDecimal("123.4");
        when(bookkeeperRepository.getTotalSpendForPeriod(any(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(total);
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String fromDate = startOfMonth.format(formatter);
        String toDate = now.format(formatter);
        String expected = String.format("За период с %s по %s вы потратили: %,.2f грн.", fromDate, toDate, total);

        String result = bookkeeperService.getWhatWasSpendDuringTheMonth(CHAT_ID);

        assertEquals(expected, result);
    }

    @Test
    void getWhatWasSpendDuringTheMonth_ifTotalFromDbIsZero_returnCorrectText() {
        BigDecimal total = BigDecimal.ZERO;
        when(bookkeeperRepository.getTotalSpendForPeriod(any(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(total);
        String expected = "Вы еще ничего не потратили в этом месяце. Отличная экономия!";

        String result = bookkeeperService.getWhatWasSpendDuringTheMonth(CHAT_ID);

        assertEquals(expected, result);
    }

    @Test
    void getAllHistory_WhenNoRecords_ShouldReturnEmptyMessage() {
        int pageNumber = 0;
        Page<BookkeeperRecord> emptyPage = Page.empty();
        when(bookkeeperRepository.findAllByTelegramChatIdOrderByPurchaseDateDesc(eq(CHAT_ID), any(Pageable.class)))
                .thenReturn(emptyPage);

        String result = bookkeeperService.getAllHistory(CHAT_ID, pageNumber);

        assertThat(result).isEqualTo("История отсутствует или страница пуста.");
    }

    @Test
    void getAllHistory_WhenRecordsExist_ShouldGroupAndFormatCorrectly() {
        int pageNumber = 0;
        LocalDate today = LocalDate.of(2026, 7, 13);
        LocalDate yesterday = LocalDate.of(2026, 7, 12);
        List<BookkeeperRecord> content = List.of(
                BookkeeperRecord.builder()
                        .telegramChatId(CHAT_ID)
                        .itemName("Coffee")
                        .price(new BigDecimal("65.50"))
                        .purchaseDate(today)
                        .build(),
                BookkeeperRecord.builder()
                        .telegramChatId(CHAT_ID)
                        .itemName("Products")
                        .price(new BigDecimal("450.00"))
                        .purchaseDate(today)
                        .build(),
                BookkeeperRecord.builder()
                        .telegramChatId(CHAT_ID)
                        .itemName("Fuel")
                        .price(new BigDecimal("1200.50"))
                        .purchaseDate(yesterday)
                        .build()
        );
        Pageable pageable = PageRequest.of(pageNumber, 5);
        Page<BookkeeperRecord> pageResult = new PageImpl<>(content, pageable, 3);
        when(bookkeeperRepository.findAllByTelegramChatIdOrderByPurchaseDateDesc(eq(CHAT_ID), any(Pageable.class)))
                .thenReturn(pageResult);

        String result = bookkeeperService.getAllHistory(CHAT_ID, pageNumber);

        assertThat(result)
                .contains("📅 Дата: 13.07.2026")
                .contains("Coffee", "65,50")
                .contains("Products", "450,00")
                .contains("📅 Дата: 12.07.2026")
                .contains("Fuel")
                .contains("1")
                .contains("200,50")
                .contains("грн.");
        assertThat(result)
                .contains("--- Стр. 1 из 1 ---");
    }
}
