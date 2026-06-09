package com.orgtgbot.entity.user;

import com.orgtgbot.bot.callback.GeneralFields;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "telegram_chat_id")
    private UserEntry user;
}
