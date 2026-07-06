package com.orgtgbot.service.services.bookkeeper;

import com.orgtgbot.dto.bookkeeper.ReceiptItemDto;
import com.orgtgbot.entity.bookkeeper.BookkeeperRecord;
import com.orgtgbot.repository.BookkeeperRepository;
import com.orgtgbot.service.filehandler.image.ImageService;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookkeeperService {

    private final BookkeeperRepository bookkeeperRepository;
    private final GeminiParserService geminiParserService;
    private final ImageService imageService;

    public BookkeeperService(BookkeeperRepository bookkeeperRepository,
                             GeminiParserService geminiParserService,
                             @Lazy ImageService imageService) {
        this.bookkeeperRepository = bookkeeperRepository;
        this.geminiParserService = geminiParserService;
        this.imageService = imageService;
    }

    @Transactional
    public void addReceipt(Long chatId, String fileId) {
        byte[] imageBytes = imageService.downloadImageBytes(fileId);
        String mimeType = imageService.getMimeType(fileId);

        List<ReceiptItemDto> parsedItems = geminiParserService.parseReceiptImage(imageBytes, mimeType);

        LocalDate today = LocalDate.now();
        List<BookkeeperRecord> records = parsedItems.stream()
                .map(dto -> BookkeeperRecord.builder()
                        .telegramChatId(chatId)
                        .itemName(dto.item())
                        .price(dto.price())
                        .purchaseDate(today)
                        .build())
                .toList();

        bookkeeperRepository.saveAll(records);
    }

    @Transactional(readOnly = true)
    public String getWhatWasSpendDuringTheMonth(Long chatId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);

        BigDecimal total = bookkeeperRepository.getTotalSpendForPeriod(chatId, startOfMonth, now);

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return "Вы еще ничего не потратили в этом месяце. Отличная экономия!";
        }

        return String.format("За текущий месяц вы потратили: %,.2f грн.", total);
    }
}
