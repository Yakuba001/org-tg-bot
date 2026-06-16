package com.orgtgbot.entity.reminder;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_chat_id")
    private Long telegramChatId;

    @Column(nullable = false)
    private String text;

    @Column(name = "target_time", nullable = false)
    private LocalDateTime targetTime;
}
