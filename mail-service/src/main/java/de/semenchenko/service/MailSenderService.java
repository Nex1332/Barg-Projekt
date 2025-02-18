package de.semenchenko.service;

import de.semenchenko.MailParams;
import reactor.core.publisher.Mono;

public interface MailSenderService {
    Mono<Void> send(MailParams mailParams);
}
