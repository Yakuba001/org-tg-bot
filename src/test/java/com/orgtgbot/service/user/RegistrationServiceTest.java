package com.orgtgbot.service.user;

import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.user.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private UserWorkspaceRepository userWorkspaceRepository;

    @Captor
    private ArgumentCaptor<UserWorkspace> workspaceCaptor;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void registrationNewDriver_fueledAllNewFields() {
        Long chatId = 12345L;
        String name = "Иван";

        registrationService.registerNewDriver(chatId, name);

        verify(userWorkspaceRepository, times(1)).save(workspaceCaptor.capture());
        UserWorkspace savedWorkspace = workspaceCaptor.getValue();

        assertThat(savedWorkspace).isNotNull();

        assertThat(savedWorkspace.getUser())
                .isNotNull()
                .satisfies(user -> {
                    assertThat(user.getTelegramChatId()).isEqualTo(chatId);
                    assertThat(user.getName()).isEqualTo(name);
                    assertThat(user.getRole()).isEqualTo("USER");
                    assertThat(user.getStep()).isEqualTo("MAIN_MENU");
                });
        assertThat(savedWorkspace.getGeneralEntry())
                .isNotNull()
                .satisfies(general -> {
                    assertThat(general.getName()).isEqualTo(name);
                    assertThat(general.getDate()).isEqualTo(" ");
                    assertThat(general.getCarModel()).isEqualTo(" ");
                    assertThat(general.getCarNumber()).isEqualTo(" ");
                    assertThat(general.getStartWeekProbeg()).isEqualTo(0);
                    assertThat(general.getEndWeekProbeg()).isEqualTo(0);
                    assertThat(general.getStartBalanceLitres()).isEqualTo(BigDecimal.ZERO);
                    assertThat(general.getEndBalanceLitres()).isEqualTo(BigDecimal.ZERO);
                    assertThat(general.getTotalWeekKm()).isEqualTo(0);
                    assertThat(general.getFuelNorm()).isEqualTo(BigDecimal.ZERO);
                    assertThat(general.getLitresSpend()).isEqualTo(BigDecimal.ZERO);
                    assertThat(general.getFueling()).isEqualTo(0);
                });
        assertThat(savedWorkspace.getDatesEntries())
                .hasSize(5);
        assertThat(savedWorkspace.getReportEntries())
                .hasSize(5)
                .extracting(ReportEntry::getDayNumber)
                .containsExactly(1, 2, 3, 4, 5);
    }
}
