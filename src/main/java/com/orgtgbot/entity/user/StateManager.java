package com.orgtgbot.entity.user;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "state_manager")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateManager {

    @Id
    @Column(name = "telegram_chat_id")
    private Long telegramChatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_field", nullable = false)
    @Builder.Default
    private GeneralFields currentField = GeneralFields.NONE;

    @Column(name = "last_bot_menu_id")
    private Integer lastBotMenuId;

    @Column(name = "user_last_activity_time")
    private LocalDateTime userLastActivityTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "telegram_chat_id",
            referencedColumnName = "telegram_chat_id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private UserEntry user;
}
