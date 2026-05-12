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

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    @Column(nullable = false)
    private Integer kilometers;
}
