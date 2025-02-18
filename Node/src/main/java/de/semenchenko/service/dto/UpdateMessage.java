package de.semenchenko.service.dto;

import de.semenchenko.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@AllArgsConstructor
public class UpdateMessage {
    private AppUser appUser;
    private Update update;
}
