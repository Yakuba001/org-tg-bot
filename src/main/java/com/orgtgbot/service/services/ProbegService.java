package com.orgtgbot.service.services;

import com.orgtgbot.dto.ReportEntryDto;
import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.exception.exceptions.service.DayNotFoundException;
import com.orgtgbot.exception.exceptions.service.WorkspaceNotFoundException;
import com.orgtgbot.mapper.ProbegMapper;
import com.orgtgbot.repository.UserWorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProbegService {

    private final UserWorkspaceRepository userWorkspaceRepository;
    private final ProbegMapper probegMapper;

    @Transactional
    public void clearAll(Long chatId) {
        getAllEntities(chatId).forEach(entry -> {
            entry.setMorningKm(0);
            entry.setEveningKm(0);
            entry.setTotalKm(0);
            entry.setRoute(" ");
        });
    }

    @Transactional
    public void updateProbegInfo(Long chatId, int dayNumber, ReportEntryDto dto) {
        ReportEntry entry = getReportEntryEntity(chatId, dayNumber);
        probegMapper.updateEntityFromDto(dto, entry);
        recalculateTotalKm(entry);
    }

    public ReportEntryDto getReportEntryDto(Long chatId, int dayNumber) {
        ReportEntry entry = getReportEntryEntity(chatId, dayNumber);
        return probegMapper.toDto(entry);
    }

    public List<ReportEntryDto> getAllDto(Long chatId) {
        List<ReportEntry> entries = getAllEntities(chatId);
        return probegMapper.toDtoList(entries);
    }

    private List<ReportEntry> getAllEntities(Long chatId) {
        UserWorkspace workspace = userWorkspaceRepository.findByUser_TelegramChatId(chatId)
                .orElseThrow(() -> new WorkspaceNotFoundException(chatId, "Workspace not found in getAll"));
        return workspace.getReportEntries();
    }

    private ReportEntry getReportEntryEntity(Long chatId, int dayNumber) {
        UserWorkspace workspace = userWorkspaceRepository.findByUser_TelegramChatId(chatId)
                .orElseThrow(() -> new WorkspaceNotFoundException(chatId, "Workspace not found"));

        return workspace.getReportEntries().stream()
                .filter(e -> e.getDayNumber() == dayNumber)
                .findFirst()
                .orElseThrow(() -> new DayNotFoundException(chatId, "Day not found"));
    }

    private void recalculateTotalKm(ReportEntry entry) {
        Integer morning = entry.getMorningKm();
        Integer evening = entry.getEveningKm();
        if (morning != null && morning != 0 && evening != null && evening != 0) {
            int range = 1000;
            int result = (evening - morning + range) % range;
            entry.setTotalKm(result);
        }
    }
}
