package com.orgtgbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "general_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String date;

    @Column(name = "car_model", nullable = false)
    private String carModel;

    @Column(name = "car_number", nullable = false)
    private String carNumber;

    @Column(name = "start_week_probeg", nullable = false)
    private Integer startWeekProbeg;

    @Column(name = "end_week_probeg", nullable = false)
    private Integer endWeekProbeg;

    @Column(name = "start_balance_litres", nullable = false)
    private BigDecimal startBalanceLitres;

    @Column(name = "end_balance_litres", nullable = false)
    private BigDecimal endBalanceLitres;

    @Column(name = "total_week_km", nullable = false)
    private Integer totalWeekKm;

    @Column(name = "fuel_norm", nullable = false)
    private BigDecimal fuelNorm;

    @Column(name = "litres_spend", nullable = false)
    private BigDecimal litresSpend;

    @Column(nullable = false)
    private Integer fueling;
}
