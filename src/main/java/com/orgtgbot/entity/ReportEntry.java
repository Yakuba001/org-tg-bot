package com.orgtgbot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(name = "morning_km", nullable = false)
    private Integer morningKm;

    @Column(name = "evening_km", nullable = false)
    private Integer eveningKm;
}
