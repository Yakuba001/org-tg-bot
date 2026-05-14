package com.orgtgbot.service;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.repository.ReportEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProbegService {

    private final ReportEntryRepository repository;

    @Transactional
    public void firstStart() {
        if (getAll() != null && getAll().size() < 5) {
            for (int i = 0; i < 5; i++) {
                repository.save(ReportEntry.builder()
                        .dayNumber(i + 1)
                        .route(" ")
                        .morningKm(0)
                        .eveningKm(0)
                        .totalKm(0)
                        .build());
            }
        }
    }

    @Transactional
    public void setMorningKm(UserState state, Integer km) {
        List<ReportEntry> morningKm = getAll();
        switch (state) {
            case PROBEG_MORNING_MONDAY -> morningKm.getFirst().setMorningKm(km);
            case PROBEG_MORNING_TUESDAY -> morningKm.get(1).setMorningKm(km);
            case PROBEG_MORNING_WEDNESDAY -> morningKm.get(2).setMorningKm(km);
            case PROBEG_MORNING_THURSDAY -> morningKm.get(3).setMorningKm(km);
            case PROBEG_MORNING_FRIDAY -> morningKm.get(4).setMorningKm(km);
        }
    }

    @Transactional
    public void setEveningKm(UserState state, Integer km) {
        List<ReportEntry> eveningKm = getAll();
        switch (state) {
            case PROBEG_EVENING_MONDAY -> eveningKm.getFirst().setEveningKm(km);
            case PROBEG_EVENING_TUESDAY -> eveningKm.get(1).setEveningKm(km);
            case PROBEG_EVENING_WEDNESDAY -> eveningKm.get(2).setEveningKm(km);
            case PROBEG_EVENING_THURSDAY -> eveningKm.get(3).setEveningKm(km);
            case PROBEG_EVENING_FRIDAY -> eveningKm.get(4).setEveningKm(km);
        }
    }

    @Transactional
    public void setTotalKm(UserState state, Integer km) {
        List<ReportEntry> totalKm = getAll();
        switch (state) {
            case PROBEG_TOTAL_MONDAY -> totalKm.getFirst().setTotalKm(km);
            case PROBEG_TOTAL_TUESDAY -> totalKm.get(1).setTotalKm(km);
            case PROBEG_TOTAL_WEDNESDAY -> totalKm.get(2).setTotalKm(km);
            case PROBEG_TOTAL_THURSDAY -> totalKm.get(3).setTotalKm(km);
            case PROBEG_TOTAL_FRIDAY -> totalKm.get(4).setTotalKm(km);
        }
    }

    @Transactional
    public void setRoute(UserState state, String route) {
        List<ReportEntry> routeList = getAll();
        switch (state) {
            case ROUTE_MONDAY -> routeList.getFirst().setRoute(route);
            case ROUTE_TUESDAY -> routeList.get(1).setRoute(route);
            case ROUTE_WEDNESDAY -> routeList.get(2).setRoute(route);
            case ROUTE_THURSDAY -> routeList.get(3).setRoute(route);
            case ROUTE_FRIDAY -> routeList.get(4).setRoute(route);
        }
    }

    @Transactional
    public String getMorningKm(Buttons button) {
        List<ReportEntry> morningKm = getAll();
        switch (button) {
            case SET_MORNING_MONDAY_KM -> {
                return morningKm.getFirst().getMorningKm().toString();
            }
            case SET_MORNING_TUESDAY_KM -> {
                return morningKm.get(1).getMorningKm().toString();
            }
            case SET_MORNING_WEDNESDAY_KM -> {
                return morningKm.get(2).getMorningKm().toString();
            }
            case SET_MORNING_THURSDAY_KM -> {
                return morningKm.get(3).getMorningKm().toString();
            }
            case SET_MORNING_FRIDAY_KM -> {
                return morningKm.get(4).getMorningKm().toString();
            }
            default -> {
                return "0";
            }
        }
    }

    @Transactional
    public String getEveningKm(Buttons button) {
        List<ReportEntry> eveningKm = getAll();
        switch (button) {
            case SET_EVENING_MONDAY_KM -> {
                return eveningKm.getFirst().getEveningKm().toString();
            }
            case SET_EVENING_TUESDAY_KM -> {
                return eveningKm.get(1).getEveningKm().toString();
            }
            case SET_EVENING_WEDNESDAY_KM -> {
                return eveningKm.get(2).getEveningKm().toString();
            }
            case SET_EVENING_THURSDAY_KM -> {
                return eveningKm.get(3).getEveningKm().toString();
            }
            case SET_EVENING_FRIDAY_KM -> {
                return eveningKm.get(4).getEveningKm().toString();
            }
            default -> {
                return "0";
            }
        }
    }

    @Transactional
    public String getTotalKm(Buttons button) {
        List<ReportEntry> totalKm = getAll();
        switch (button) {
            case SET_TOTAL_MONDAY_KM -> {
                return totalKm.getFirst().getTotalKm().toString();
            }
            case SET_TOTAL_TUESDAY_KM -> {
                return totalKm.get(1).getTotalKm().toString();
            }
            case SET_TOTAL_WEDNESDAY_KM -> {
                return totalKm.get(2).getTotalKm().toString();
            }
            case SET_TOTAL_THURSDAY_KM -> {
                return totalKm.get(3).getTotalKm().toString();
            }
            case SET_TOTAL_FRIDAY_KM -> {
                return totalKm.get(4).getTotalKm().toString();
            }
            default -> {
                return "0";
            }
        }
    }

    @Transactional
    public String getRoute(Buttons button) {
        List<ReportEntry> route = getAll();
        switch (button) {
            case SET_MONDAY_ROUTE -> {
                return route.getFirst().getRoute();
            }
            case SET_TUESDAY_ROUTE -> {
                return route.get(1).getRoute();
            }
            case SET_WEDNESDAY_ROUTE -> {
                return route.get(2).getRoute();
            }
            case SET_THURSDAY_ROUTE -> {
                return route.get(3).getRoute();
            }
            case SET_FRIDAY_ROUTE -> {
                return route.get(4).getRoute();
            }
            default -> {
                return " ";
            }
        }
    }

    public List<ReportEntry> getAll() {
        return repository.findAllByOrderByDayNumberAsc();
    }
}
