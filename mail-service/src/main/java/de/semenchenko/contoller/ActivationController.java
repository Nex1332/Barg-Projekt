package de.semenchenko.contoller;

import de.semenchenko.service.UserActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequestMapping("/user")
@RestController
public class ActivationController {
    private final UserActivationService userActivationService;

    public ActivationController(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @GetMapping("/activation")
    public Mono<ResponseEntity<String>> activation(@RequestParam("id") Long id) {
        return userActivationService.activation(id)
                .flatMap(res -> {
                    if (res) {
                        return Mono.just(ResponseEntity.ok("Nice"));
                    } else {
                        return Mono.just(ResponseEntity.internalServerError().body("Activation failed"));
                    }
                });
    }
}
