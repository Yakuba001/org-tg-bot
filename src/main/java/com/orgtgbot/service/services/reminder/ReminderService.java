package com.orgtgbot.service.services.reminder;

import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.repository.ReminderRepository;
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
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Transactional
    public void addRemind(Long chatId, String rawText) {
        log.info("[REMINDER] Попытка добавить напоминание для chatId {}: {}", chatId, rawText);

        ReminderDto parsedDto = new ReminderDto(
                "Робот-напоминалка (Заглушка): " + rawText,
                LocalDateTime.now().plusMinutes(10)
        );

        ReminderEntity entity = ReminderEntity.builder()
                .telegramChatId(chatId)
                .text(parsedDto.text())
                .targetTime(parsedDto.targetTime())
                .build();

        reminderRepository.save(entity);

        log.info("[REMINDER] Напоминание успешно сохранено в БД для chatId {}", chatId);
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
