package pl.zajonz.emailsender.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import pl.zajonz.emailsender.event.InfoEvent;
import pl.zajonz.emailsender.model.InfoMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EmailServiceUnitTest {

    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender sender;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Captor
    private ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor;

    @Test
    void testSend_ShouldInvokeSender() {
        //given
        InfoMessage infoMessage = new InfoMessage();
        infoMessage.setBook_author("testA");
        infoMessage.setBook_title("testT");
        infoMessage.setBook_category("testC");
        infoMessage.setBookId(1);
        infoMessage.setUser_firstName("testF");
        infoMessage.setUser_lastName("testL");
        infoMessage.setEmail("test@test.pl");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(infoMessage.getEmail());
        message.setSubject("Wypożycz nowo dodaną książkę!");
        message.setText("Cześć " + infoMessage.getUser_lastName() + " " + infoMessage.getUser_firstName() +
                ", zauważyliśmy że obserwujesz kategorię " + infoMessage.getBook_category() + " " +
                "i chcielibyśmy cię poinformować że właśnie dodano nową książkę \" " +
                infoMessage.getBook_title() + "\" - " + infoMessage.getBook_author());

        //when
        emailService.send(infoMessage);

        //then
        verify(eventPublisher, times(1)).publishEvent(any(InfoEvent.class));
        verify(sender, times(1)).send(simpleMailMessageArgumentCaptor.capture());
        SimpleMailMessage capturedSimpleMailMessage = simpleMailMessageArgumentCaptor.getValue();
        assertEquals(message, capturedSimpleMailMessage);
    }
}