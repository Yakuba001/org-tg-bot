package com.orgtgbot.service.services.user;

import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.entity.GeneralEntry;
import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.entity.user.UserEntry;
import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.repository.UserWorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationService {

    private final UserWorkspaceRepository userWorkspaceRepository;

    public void registerNewDriver(Long telegramChatId, String firstName) {
        UserEntry user = UserEntry.builder()
                .telegramChatId(telegramChatId)
                .name(firstName)
                .role("USER")
                .step("MAIN_MENU")
                .build();

        GeneralEntry generalEntry = GeneralEntry.builder()
                .name(firstName)
                .date(" ")
                .carModel(" ")
                .carNumber(" ")
                .startWeekProbeg(0)
                .endWeekProbeg(0)
                .startBalanceLitres(BigDecimal.ZERO)
                .endBalanceLitres(BigDecimal.ZERO)
                .totalWeekKm(0)
                .fuelNorm(BigDecimal.ZERO)
                .litresSpend(BigDecimal.ZERO)
                .fueling(0)
                .build();

        List<DatesEntry> dates = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dates.add(DatesEntry.builder()
                    .date(" ")
                    .build());
        }

        List<ReportEntry> reports = new ArrayList<>();
        for (int dayNum = 1; dayNum <= 5; dayNum++) {
            reports.add(ReportEntry.builder()
                    .dayNumber(dayNum)
                    .route(" ")
                    .morningKm(0)
                    .eveningKm(0)
                    .totalKm(0)
                    .build());
        }

        UserWorkspace workspace = UserWorkspace.builder()
                .user(user)
                .generalEntry(generalEntry)
                .datesEntries(dates)
                .reportEntries(reports)
                .build();

        userWorkspaceRepository.save(workspace);
    }
}
