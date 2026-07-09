package com.orgtgbot.integration.service.bookkeeper;

import com.orgtgbot.dto.bookkeeper.ReceiptItemDto;
import com.orgtgbot.entity.bookkeeper.BookkeeperRecord;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.repository.BookkeeperRepository;
import com.orgtgbot.service.filehandler.image.ImageChecker;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class BookkeeperServiceIT extends BaseIntegrationTest {

    @Autowired private BookkeeperService bookkeeperService;
    @Autowired private BookkeeperRepository bookkeeperRepository;
    @MockBean private GeminiParserService geminiParserService;
    @MockBean private ImageChecker imageChecker;

    private static final Long CHAT_ID = 123L;
    private static final String FILE_ID = "test_file_id_123";

    @BeforeEach
    void setUp() {
        bookkeeperRepository.deleteAll();
    }

    @Test
    void addReceipt_shouldSuccessfullyDownloadParseAndSaveToPostgres() {
        byte[] fakeImageBytes = new byte[]{1, 2, 3, 4};
        String fakeMimeType = "image/jpeg";
        when(imageChecker.downloadImageBytes(FILE_ID)).thenReturn(fakeImageBytes);
        when(imageChecker.getMimeType(FILE_ID)).thenReturn(fakeMimeType);
        List<ReceiptItemDto> fakeParsedItems = List.of(
                new ReceiptItemDto("Молоко Фермерское", new BigDecimal("45.50")),
                new ReceiptItemDto("Батон Нарезной", new BigDecimal("18.00"))
        );
        when(geminiParserService.parseReceiptImage(fakeImageBytes, fakeMimeType)).thenReturn(fakeParsedItems);

        bookkeeperService.addReceipt(CHAT_ID, FILE_ID);

        List<BookkeeperRecord> savedRecords = bookkeeperRepository.findAll();
        assertEquals(2, savedRecords.size());
        BookkeeperRecord record1 = savedRecords.stream()
                .filter(r -> r.getItemName().equals("Молоко Фермерское"))
                .findFirst()
                .orElseThrow();
        assertNotNull(record1.getId());
        assertEquals(CHAT_ID, record1.getTelegramChatId());
        assertEquals(new BigDecimal("45.50"), record1.getPrice());
        assertNotNull(record1.getPurchaseDate());
    }

    @Test
    void getWhatWasSpendDuringTheMonth_ShouldReturnZero_whenNoDataInDb() {
        String result = bookkeeperService.getWhatWasSpendDuringTheMonth(CHAT_ID);

        assertEquals("Вы еще ничего не потратили в этом месяце. Отличная экономия!", result);
    }

    @Test
    void getWhatWasSpendDuringTheMonth_ShouldReturnCorrectText() {
        BookkeeperRecord r1 = BookkeeperRecord.builder()
                .telegramChatId(CHAT_ID)
                .price(new BigDecimal("123.45"))
                .itemName("Test item")
                .purchaseDate(LocalDate.now())
                .createdAt(null)
                .build();
        BookkeeperRecord r2 = BookkeeperRecord.builder()
                .telegramChatId(CHAT_ID)
                .price(new BigDecimal("223.85"))
                .itemName("Test item")
                .purchaseDate(LocalDate.now())
                .createdAt(null)
                .build();
        bookkeeperRepository.saveAll(List.of(r1, r2));

        String result = bookkeeperService.getWhatWasSpendDuringTheMonth(CHAT_ID);
        BigDecimal total = r1.getPrice().add(r2.getPrice());

        assertEquals(String.format("За текущий месяц вы потратили: %,.2f грн.", total), result);
    }
}
