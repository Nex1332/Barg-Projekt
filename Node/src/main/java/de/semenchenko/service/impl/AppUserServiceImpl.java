package de.semenchenko.service.impl;

import de.semenchenko.dao.ReactiveAppUserDAO;
import de.semenchenko.model.AppUser;
import de.semenchenko.service.AppUserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static de.semenchenko.model.enums.BotState.WAIT_FOR_EMAIL_STATE;
import static de.semenchenko.model.enums.UserState.REGISTERED;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final ReactiveAppUserDAO reactiveAppUserDAO;

    public AppUserServiceImpl(ReactiveAppUserDAO reactiveAppUserDAO) {
        this.reactiveAppUserDAO = reactiveAppUserDAO;
    }

    @Override
    public Mono<String> registerUser(AppUser appUser) {
        if (appUser.getUserState().equals(REGISTERED)) {
            return Mono.just("Вы уже зарегистрированы!");
        } else if (appUser.getEmail() != null) {
            return Mono.just("Вам на почту уже было отправлено письмо");
        }

        appUser.setBotState(WAIT_FOR_EMAIL_STATE);

        return reactiveAppUserDAO.save(appUser)
                .then(Mono.just("Введите, пожалуйста ваш email для Регистрации"));
    }

    @Override
    public Mono<String> setEmail(AppUser appUser, String email) {
        return null;
    }

    @Override
    public Mono<String> setCity(AppUser appUser, String city) {
        return null;
    }

}
