package com.orgtgbot.service.services;

import com.orgtgbot.dto.GeneralEntryDto;
import com.orgtgbot.entity.GeneralEntry;
import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.exception.exceptions.service.WorkspaceNotFoundException;
import com.orgtgbot.mapper.GeneralMapper;
import com.orgtgbot.repository.UserWorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralService {

    private final UserWorkspaceRepository userWorkspaceRepository;
    private final GeneralMapper generalMapper;

    @Transactional
    public void updateGeneralInfo(Long chatId, GeneralEntryDto updateDto) {
        GeneralEntry entry = getSingleEntry(chatId);
        generalMapper.updateEntityFromDto(updateDto, entry);
    }

    public GeneralEntryDto getSingleDto(Long chatId) {
        GeneralEntry entry = getSingleEntry(chatId);
        return generalMapper.toDto(entry);
    }

    private GeneralEntry getSingleEntry(Long chatId) {
       UserWorkspace workspace = userWorkspaceRepository.findByUser_TelegramChatId(chatId)
                .orElseThrow(() -> new WorkspaceNotFoundException(
                        chatId,
                        "Workspace not found in GeneralService -> getSingleEntry"));
        return workspace.getGeneralEntry();
    }
}
