package de.semenchenko.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("app_user")
public class AppUser {
    @Id
    private Long id;
    @Column("telegram_user_id")
    private Long telegramUserId;
    @Column("chat_id")
    private Long chatId;
    private String username;
    private String email;
    private String city;
    @Column("user_state")
    private String userState;
    @Column("bot_state")
    private String botState;
}
