package pl.zajonz.emailsender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pl.zajonz.emailsender.model.InfoMessage;

@Service
@RequiredArgsConstructor
public class Listener {

    private final EmailService emailService;

    @RabbitListener(queues = "${email-info-queue}")
    public void listen(InfoMessage message) {
        emailService.send(message);
    }


}
