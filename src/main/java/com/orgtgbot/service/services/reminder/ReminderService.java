package com.orgtgbot.service.services.reminder;

import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.mapper.reminder.ReminderMapper;
import com.orgtgbot.repository.ReminderRepository;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import com.orgtgbot.service.services.user.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final GeminiParserService geminiParserService;
    private final UserStateService userStateService;
    private final ReminderMapper reminderMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Transactional
    public void addRemind(Long chatId, String rawText) {
        LocalDateTime userTime = getUserTime(chatId);
        ReminderDto parsedDto = geminiParserService.parseReminder(rawText, userTime);
        saveReminder(chatId, parsedDto);
    }

    @Transactional
    public void addVoiceRemind(Long chatId, byte[] audioBytes) {
        LocalDateTime userTime = getUserTime(chatId);
        ReminderDto parsedDto = geminiParserService.parseVoiceReminder(audioBytes, userTime);
        saveReminder(chatId, parsedDto);
    }

    @Transactional
    public void cleanReminders() {
        LocalDateTime timeAgo = LocalDateTime.now(ZoneId.of("Europe/Kiev")).minusDays(1);
        reminderRepository.deleteOldSentReminders(timeAgo);
    }

    @Transactional
    public void markAsSent(Long id) {
        reminderRepository.findById(id).ifPresent(reminder -> reminder.setSent(true));
    }

    public String getAllRemindersFormatted(Long chatId) {
        List<ReminderEntity> reminders = reminderRepository.findAllByTelegramChatId(chatId);
        if (reminders.isEmpty()) {
            return "\nУ вас пока нет активных напоминаний.";
        }
        return "\nВаши напоминания:\n" + reminders.stream()
                .map(r -> "🔹 " + r.getTargetTime().format(FORMATTER) + " — " + r.getText())
                .collect(Collectors.joining("\n"));
    }

    public List<ReminderEntity> getAllReminders() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Kiev"));
        return reminderRepository.findAllByIsSentFalseAndTargetTimeLessThanEqual(now);
    }

    private LocalDateTime getUserTime(Long chatId) {
        return userStateService.getUserLastActivityTime(chatId);
    }

    private void saveReminder(Long chatId, ReminderDto parsedDto) {
        ReminderEntity entity = reminderMapper.toEntity(parsedDto, chatId);
        reminderRepository.save(entity);
    }
}
