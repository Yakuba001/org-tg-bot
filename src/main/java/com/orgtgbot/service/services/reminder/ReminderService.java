package com.orgtgbot.service.services.reminder;

import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.repository.ReminderRepository;
import com.orgtgbot.repository.StateManagerRepository;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import com.orgtgbot.service.services.user.UserStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final GeminiParserService geminiParserService;

    private final UserStateService userStateService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Transactional
    public void addRemind(Long chatId, String rawText) {
        log.info("[REMINDER] Попытка добавить напоминание для chatId {}: {}", chatId, rawText);

        StateManager stateManager = userStateService.getCurrentState(chatId);
        LocalDateTime userTime = stateManager.getUserLastActivityTime() == null ?
                LocalDateTime.now() : stateManager.getUserLastActivityTime();

        ReminderDto parsedDto = geminiParserService.parseReminder(rawText, userTime);

        ReminderEntity entity = ReminderEntity.builder()
                .telegramChatId(chatId)
                .text(parsedDto.text())
                .targetTime(parsedDto.targetTime())
                .build();

        reminderRepository.save(entity);
        log.info("[REMINDER] Напоминание успешно сохранено в БД. Время: {}, Текст: {}",
                parsedDto.targetTime(), parsedDto.text());
    }

    @Transactional(readOnly = true)
    public String getAllRemindersFormatted(Long chatId) {
        List<ReminderEntity> reminders = reminderRepository.findAllByTelegramChatId(chatId);

        if (reminders.isEmpty()) {
            return "\nУ вас пока нет активных напоминаний.";
        }

        return "\nВаши напоминания:\n" + reminders.stream()
                .map(r -> "🔹 " + r.getTargetTime().format(FORMATTER) + " — " + r.getText())
                .collect(Collectors.joining("\n"));
    }
}
