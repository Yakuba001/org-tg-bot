package com.orgtgbot.service.services;

import com.orgtgbot.dto.GeneralUpdateDto;
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
@Transactional
public class GeneralService {

    private final UserWorkspaceRepository userWorkspaceRepository;
    private final GeneralMapper generalMapper;

    public void updateGeneralInfo(Long chatId, GeneralUpdateDto updateDto) {
        GeneralEntry entry = getSingleEntry(chatId);
        generalMapper.updateEntityFromDto(updateDto, entry);
    }

    @Transactional(readOnly = true)
    public GeneralEntry getSingleEntry(Long chatId) {
       UserWorkspace workspace = userWorkspaceRepository.findByUser_TelegramChatId(chatId)
                .orElseThrow(() -> new WorkspaceNotFoundException(
                        chatId,
                        "Workspace not found in GeneralService -> getSingleEntry"));
        return workspace.getGeneralEntry();
    }
}
