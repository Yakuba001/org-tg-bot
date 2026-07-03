package com.orgtgbot.service.services;

import com.orgtgbot.dto.ProbegUpdateDto;
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
@Transactional
public class ProbegService {

    private final UserWorkspaceRepository userWorkspaceRepository;
    private final ProbegMapper probegMapper;

    public void clearAll(Long chatId) {
        getAll(chatId).forEach(entry -> {
            entry.setMorningKm(0);
            entry.setEveningKm(0);
            entry.setTotalKm(0);
            entry.setRoute(" ");
        });
    }

    public void updateProbegInfo(Long chatId, int dayNumber, ProbegUpdateDto dto) {
        ReportEntry entry = getReportEntry(chatId, dayNumber);
        probegMapper.updateEntityFromDto(dto, entry);
        recalculateTotalKm(entry);
    }

    @Transactional(readOnly = true)
    public ReportEntry getReportEntry(Long chatId, int dayNumber) {
        UserWorkspace workspace = userWorkspaceRepository.findByUser_TelegramChatId(chatId)
                .orElseThrow(() -> new WorkspaceNotFoundException(chatId, "Workspace not found in getReportEntry"));
        return workspace.getReportEntries().stream()
                .filter(e -> e.getDayNumber() == dayNumber)
                .findFirst()
                .orElseThrow(() -> new DayNotFoundException(chatId, "Day not found"));
    }

    @Transactional(readOnly = true)
    public List<ReportEntry> getAll(Long chatId) {
        UserWorkspace workspace = userWorkspaceRepository.findByUser_TelegramChatId(chatId)
                .orElseThrow(() -> new WorkspaceNotFoundException(chatId, "Workspace not found in getAll"));
        return workspace.getReportEntries();
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
