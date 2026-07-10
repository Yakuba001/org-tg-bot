package com.orgtgbot.entity.user;

import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.entity.GeneralEntry;
import com.orgtgbot.entity.ReportEntry;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user_workspaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWorkspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntry user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "general_entry_id", nullable = false, unique = true)
    private GeneralEntry generalEntry;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "workspace_dates",
            joinColumns = @JoinColumn(name = "workspace_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "date_entry_id", nullable = false, unique = true)
    )
    @OrderBy("id ASC")
    private List<DatesEntry> datesEntries;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "workspace_reports",
            joinColumns = @JoinColumn(name = "workspace_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "report_entry_id", nullable = false, unique = true)
    )
    private List<ReportEntry> reportEntries;
}
