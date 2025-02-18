package de.semenchenko;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppUserDTO {
    private Long chatId;
    private String email;
    private String city;
    private String userState;
    private String botState;
}
