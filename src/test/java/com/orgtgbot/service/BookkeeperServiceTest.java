package com.orgtgbot.service;

import com.orgtgbot.dto.bookkeeper.ReceiptItemDto;
import com.orgtgbot.entity.bookkeeper.BookkeeperRecord;
import com.orgtgbot.repository.BookkeeperRepository;
import com.orgtgbot.service.filehandler.image.ImageService;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookkeeperServiceTest {

    @Mock private BookkeeperRepository bookkeeperRepository;
    @Mock private GeminiParserService geminiParserService;
    @Mock private ImageService imageService;

    private BookkeeperService bookkeeperService;

    private static final long CHAT_ID = 999L;

    @BeforeEach
    void setUp() {
        bookkeeperService = new BookkeeperService(bookkeeperRepository, geminiParserService, imageService);
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
        when(imageService.getMimeType(testFileId)).thenReturn(testMimeType);
        when(imageService.downloadImageBytes(testFileId)).thenReturn(testBytes);
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
        String expected = String.format("За текущий месяц вы потратили: %,.2f грн.", total);

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
}
