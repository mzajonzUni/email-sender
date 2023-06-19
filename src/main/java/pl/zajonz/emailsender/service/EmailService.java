package pl.zajonz.emailsender.service;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajonz.emailsender.event.InfoEvent;
import pl.zajonz.emailsender.model.InfoMessage;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void send(InfoMessage infoMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(infoMessage.getEmail());
        message.setSubject("Wypożycz nowo dodaną książkę!");
        message.setText("Cześć " + infoMessage.getUser_lastName() + " " + infoMessage.getUser_firstName() +
                ", zauważyliśmy że obserwujesz kategorię " + infoMessage.getBook_category() + " " +
                "i chcielibyśmy cię poinformować że właśnie dodano nową książkę \" " +
                infoMessage.getBook_title() + "\" - " + infoMessage.getBook_author());
        emailSender.send(message);

        eventPublisher.publishEvent(new InfoEvent("Email has been sent with parameters " + infoMessage));
    }
}
