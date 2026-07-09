package com.orgtgbot.integration.service.user;

import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.user.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationServiceIT extends BaseIntegrationTest {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserWorkspaceRepository userWorkspaceRepository;

    @Test
    @Transactional
    void registeredNewDrive_hasDefaultCorrectAmounts() {
        registrationService.registerNewDriver(1L, "Ivan");
        UserWorkspace savedWorkspace = userWorkspaceRepository.findByUser_TelegramChatId(1L).orElseThrow();


        assertEquals("Ivan", savedWorkspace.getUser().getName());
        assertEquals("Ivan", savedWorkspace.getGeneralEntry().getName());
        assertEquals(5, savedWorkspace.getDatesEntries().size());
        assertEquals(5, savedWorkspace.getReportEntries().size());
        userWorkspaceRepository.deleteAll();
    }
}
