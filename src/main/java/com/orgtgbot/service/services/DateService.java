package com.orgtgbot.service.services;

import com.orgtgbot.dto.DatesUpdateDto;
import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.exception.exceptions.service.DateIndexOutOfBoundException;
import com.orgtgbot.exception.exceptions.service.WorkspaceNotFoundException;
import com.orgtgbot.mapper.DateMapper;
import com.orgtgbot.repository.UserWorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DateService {

    private final UserWorkspaceRepository userWorkspaceRepository;
    private final DateMapper dateMapper;

    public void setDate(Long chatId, int dayNumber, DatesUpdateDto dto) {
        DatesEntry date = getDatesEntry(chatId, dayNumber);
        dateMapper.updateEntityFromDto(dto, date);
    }

    @Transactional(readOnly = true)
    public List<DatesEntry> getAll(Long chatId) {
        UserWorkspace workspace = userWorkspaceRepository.findByUser_TelegramChatId(chatId)
                .orElseThrow(() -> new WorkspaceNotFoundException(chatId, "Workspace not found in DateService getAll"));
        return workspace.getDatesEntries();
    }

    @Transactional(readOnly = true)
    public DatesEntry getDatesEntry(Long chatId, int dayNumber) {
        List<DatesEntry> dates = getAll(chatId);
        int index = dayNumber - 1;
        if (index < 0 || index >= dates.size()) {
            throw new DateIndexOutOfBoundException(chatId, "Day index out of bounds: " + dayNumber);
        }
        return dates.get(index);
    }
}
