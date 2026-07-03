package com.orgtgbot.service.services.reminder;

import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.repository.ReminderRepository;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import com.orgtgbot.service.services.user.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final GeminiParserService geminiParserService;
    private final UserStateService userStateService;

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

    private LocalDateTime getUserTime(Long chatId) {
        StateManager stateManager = userStateService.getCurrentState(chatId);
        return stateManager.getUserLastActivityTime() == null ?
                LocalDateTime.now() : stateManager.getUserLastActivityTime();
    }

    private void saveReminder(Long chatId, ReminderDto parsedDto) {
        ReminderEntity entity = ReminderEntity.builder()
                .telegramChatId(chatId)
                .text(parsedDto.text())
                .targetTime(parsedDto.targetTime())
                .build();

        reminderRepository.save(entity);
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
