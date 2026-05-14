package com.orgtgbot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dates_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatesEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String date;
}
