package com.orgtgbot.service.services.bookkeeper;

import com.orgtgbot.dto.bookkeeper.ReceiptItemDto;
import com.orgtgbot.entity.bookkeeper.BookkeeperRecord;
import com.orgtgbot.mapper.bookkeeper.BookkeeperMapper;
import com.orgtgbot.repository.BookkeeperRepository;
import com.orgtgbot.service.filehandler.image.ImageChecker;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookkeeperService {

    private final BookkeeperRepository bookkeeperRepository;
    private final GeminiParserService geminiParserService;
    private final ImageChecker imageChecker;
    private final BookkeeperMapper bookkeeperMapper;

    @Transactional
    public void addReceipt(Long chatId, String fileId) {
        byte[] imageBytes = imageChecker.downloadImageBytes(fileId);
        String mimeType = imageChecker.getMimeType(fileId);
        List<ReceiptItemDto> parsedItems = geminiParserService.parseReceiptImage(imageBytes, mimeType);
        LocalDate today = LocalDate.now();

        List<BookkeeperRecord> records = parsedItems.stream()
                .map(dto -> bookkeeperMapper.toEntity(dto, chatId, today))
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
