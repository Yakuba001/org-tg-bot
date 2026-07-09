package com.orgtgbot.unit.bot.update;

import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.user.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock private UserWorkspaceRepository userWorkspaceRepository;

    private RegistrationService registrationService;

    private static final Long CHAT_ID = 12345L;
    private static final String DRIVER_NAME = "Boss";

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationService(userWorkspaceRepository);
    }

    @Test
    void registerNewDriver() {
        when(userWorkspaceRepository.save(any(UserWorkspace.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        registrationService.registerNewDriver(CHAT_ID, DRIVER_NAME);

        ArgumentCaptor<UserWorkspace> workspaceCaptor = ArgumentCaptor.forClass(UserWorkspace.class);
        verify(userWorkspaceRepository).save(workspaceCaptor.capture());

        UserWorkspace capturedWorkspace = workspaceCaptor.getValue();
        assertNotNull(capturedWorkspace);

        assertNotNull(capturedWorkspace.getUser());
        assertEquals(CHAT_ID, capturedWorkspace.getUser().getTelegramChatId());
        assertEquals(DRIVER_NAME, capturedWorkspace.getUser().getName());
        assertEquals("USER", capturedWorkspace.getUser().getRole());
        assertEquals("MAIN_MENU", capturedWorkspace.getUser().getStep());

        assertNotNull(capturedWorkspace.getGeneralEntry());
        assertEquals(DRIVER_NAME, capturedWorkspace.getGeneralEntry().getName());
        assertEquals(BigDecimal.ZERO, capturedWorkspace.getGeneralEntry().getStartBalanceLitres());

        assertEquals(5, capturedWorkspace.getDatesEntries().size());
        assertEquals(5, capturedWorkspace.getReportEntries().size());

        assertEquals(1, capturedWorkspace.getReportEntries().getFirst().getDayNumber());
    }
}
