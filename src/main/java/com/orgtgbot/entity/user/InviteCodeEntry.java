package com.orgtgbot.entity.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invite_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteCodeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;
}
