package com.orgtgbot.service;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.entity.GeneralEntry;
import com.orgtgbot.repository.GeneralEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralService {

    private final GeneralEntryRepository generalEntryRepository;

    @Transactional
    public void firstStart() {
        if (getAll() == null || getAll().isEmpty()) {
            generalEntryRepository.save(GeneralEntry.builder()
                    .name(" ")
                    .date(" ")
                    .carModel(" ")
                    .carNumber(" ")
                    .startWeekProbeg(0)
                    .endWeekProbeg(0)
                    .startBalanceLitres(new BigDecimal("0.0"))
                    .endBalanceLitres(new BigDecimal("0.0"))
                    .totalWeekKm(0)
                    .fuelNorm(new BigDecimal("0.0"))
                    .litresSpend(new BigDecimal("0.0"))
                    .fueling(0)
                    .build());
        }
    }

    @Transactional
    public void setAmount(UserState state, String amount) {
        List<GeneralEntry> generalEntries = getAll();
        switch (state) {
            case DRIVER -> generalEntries.getFirst().setName(amount);
            case DATE -> generalEntries.getFirst().setDate(amount);
            case MODEL_AUTO -> generalEntries.getFirst().setCarModel(amount);
            case NUMBER_AUTO -> generalEntries.getFirst().setCarNumber(amount);
            case START_WEEK_PROBEG -> generalEntries.getFirst().setStartWeekProbeg(Integer.parseInt(amount));
            case END_WEEK_PROBEG -> generalEntries.getFirst().setEndWeekProbeg(Integer.parseInt(amount));
            case START_BALANCE_LITRES -> generalEntries.getFirst().setStartBalanceLitres(new BigDecimal(amount));
            case END_BALANCE_LITRES -> generalEntries.getFirst().setEndBalanceLitres(new BigDecimal(amount));
            case TOTAL_WEEK_KM -> generalEntries.getFirst().setTotalWeekKm(Integer.parseInt(amount));
            case FUEL_NORM -> generalEntries.getFirst().setFuelNorm(new BigDecimal(amount));
            case LITRES_SPEND -> generalEntries.getFirst().setLitresSpend(new BigDecimal(amount));
            case FUELING -> generalEntries.getFirst().setFueling(Integer.parseInt(amount));
        }
    }

    @Transactional
    public String getAmount(Buttons button) {
        List<GeneralEntry> generalEntries = getAll();
        switch (button) {
            case DRIVER -> {
                return generalEntries.getFirst().getName();
            }
            case DATA -> {
                return generalEntries.getFirst().getDate();
            }
            case MODEL_AUTO -> {
                return generalEntries.getFirst().getCarModel();
            }
            case NUMBER_AUTO -> {
                return generalEntries.getFirst().getCarNumber();
            }
            case START_WEEK_PROBEG -> {
                return generalEntries.getFirst().getStartWeekProbeg().toString();
            }
            case END_WEEK_PROBEG -> {
                return generalEntries.getFirst().getEndWeekProbeg().toString();
            }
            case START_BALANCE_LITRES -> {
                return generalEntries.getFirst().getStartBalanceLitres().toString();
            }
            case END_BALANCE_LITRES -> {
                return generalEntries.getFirst().getEndBalanceLitres().toString();
            }
            case TOTAL_WEEK_KM -> {
                return generalEntries.getFirst().getTotalWeekKm().toString();
            }
            case FUEL_NORM -> {
                return generalEntries.getFirst().getFuelNorm().toString();
            }
            case LITRES_SPEND -> {
                return generalEntries.getFirst().getLitresSpend().toString();
            }
            case FUELING -> {
                return generalEntries.getFirst().getFueling().toString();
            }
            default -> {
                return " ";
            }
        }
    }

    public List<GeneralEntry> getAll() {
        return generalEntryRepository.findAllByOrderByIdAsc();
    }
}
