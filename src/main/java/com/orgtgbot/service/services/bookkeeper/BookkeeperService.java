package com.orgtgbot.service.services.bookkeeper;

import com.orgtgbot.dto.bookkeeper.ReceiptItemDto;
import com.orgtgbot.entity.bookkeeper.BookkeeperRecord;
import com.orgtgbot.mapper.bookkeeper.BookkeeperMapper;
import com.orgtgbot.repository.BookkeeperRepository;
import com.orgtgbot.service.filehandler.image.ImageChecker;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookkeeperService {

    private final BookkeeperRepository bookkeeperRepository;
    private final GeminiParserService geminiParserService;
    private final ImageChecker imageChecker;
    private final BookkeeperMapper bookkeeperMapper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final int PAGE_SIZE = 5;

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

    public String getWhatWasSpendDuringTheMonth(Long chatId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        BigDecimal total = bookkeeperRepository.getTotalSpendForPeriod(chatId, startOfMonth, now);
        if (total.compareTo(BigDecimal.ZERO) == 0)
            return "Вы еще ничего не потратили в этом месяце. Отличная экономия!";

        String fromDate = startOfMonth.format(formatter);
        String toDate = now.format(formatter);

        return String.format("За период с %s по %s вы потратили: %,.2f грн.", fromDate, toDate, total);
    }

    public String getAllHistory(Long chatId, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<BookkeeperRecord> pageResult =
                bookkeeperRepository.findAllByTelegramChatIdOrderByPurchaseDateDesc(chatId, pageable);
        if (pageResult.isEmpty())
            return "История отсутствует или страница пуста.";

        String historyContent = pageResult.getContent().stream()
                .collect(Collectors.groupingBy(
                        record -> record.getPurchaseDate().format(formatter),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .entrySet().stream()
                .map(entry -> {
                    String dateHeader = "📅 Дата: " + entry.getKey();
                    String items = entry.getValue().stream()
                            .map(el -> String.format(
                                    Locale.GERMANY,
                                    "  - %s: %,.2f грн.",
                                    el.getItemName(),
                                    el.getPrice()))
                            .collect(Collectors.joining("\n"));
                    return dateHeader + "\n" + items;
                })
                .collect(Collectors.joining("\n\n"));
        String paginationFooter = String.format(
                "\n\n--- Стр. %d из %d ---",
                pageResult.getNumber() + 1,
                pageResult.getTotalPages()
        );

        return historyContent + paginationFooter;
    }
}
