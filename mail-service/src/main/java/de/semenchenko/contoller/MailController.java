package de.semenchenko.contoller;

import de.semenchenko.MailParams;
import de.semenchenko.service.MailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequestMapping("/mail")
@RestController
public class MailController {
    private final MailSenderService mailSenderService;

    public MailController(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @PostMapping("/send")
    public Mono<ResponseEntity<String>> sendActivationMail(@RequestBody MailParams mailParams) {
        return mailSenderService.send(mailParams)
                .then(Mono.just(ResponseEntity.ok("Ok")));
    }
}
