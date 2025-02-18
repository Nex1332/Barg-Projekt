package de.semenchenko.service.impl;

import de.semenchenko.MailParams;
import de.semenchenko.service.MailSenderService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j
@Service
public class MailSenderServiceImpl implements MailSenderService {
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.activation.uri}")
    private String activationServiceUri;
    private final JavaMailSender javaMailSender;

    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public Mono<Void> send(MailParams mailParams) {
        return Mono.fromRunnable(() -> {
            var subject = "Account activation";
            var messageBody = getActivationMailBody(mailParams.getTelegramUserId());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(emailFrom);
            mailMessage.setTo(mailParams.getEmailTo());
            mailMessage.setSubject(subject);
            mailMessage.setText(messageBody);

            javaMailSender.send(mailMessage);
        }).then();
    }

    private String getActivationMailBody(Long telegramUserId) {
        return String.format("To complete registration, follow the link: " +
                activationServiceUri + telegramUserId);
    }
}
