package de.semenchenko.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.semenchenko.model.enums.BotState;
import de.semenchenko.model.enums.UserState;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table("app_user")
public class AppUser {
    @Id
    private Long telegramUserId;
    private String username;
    private String email;
    private UserState userState;
    private BotState botState;

}



