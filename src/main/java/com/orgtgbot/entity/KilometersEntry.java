package com.orgtgbot.entity;

import jakarta.persistence.*;
import lombok.*;

//@Entity
//@Table(name = "kilometers_entry")
//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KilometersEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "morning_km", nullable = false)
    private Integer morningKm;

    @Column(name = "evening_km", nullable = false)
    private Integer eveningKm;
}
