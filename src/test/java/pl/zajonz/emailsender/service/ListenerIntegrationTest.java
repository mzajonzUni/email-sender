package pl.zajonz.emailsender.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import pl.zajonz.emailsender.model.InfoMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class ListenerIntegrationTest {

    @Autowired
    private Listener listener;
    @SpyBean
    private EmailService emailService;
    @MockBean
    private JavaMailSender sender;
    @Captor
    private ArgumentCaptor<InfoMessage> infoMessageArgumentCaptor;
    @Captor
    private ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor;

    @Test
    void testListen_ShouldInvokeEmailService() {
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
        listener.listen(infoMessage);

        //then
        verify(emailService, times(1)).send(infoMessageArgumentCaptor.capture());
        InfoMessage capturedInfoMessage = infoMessageArgumentCaptor.getValue();
        assertEquals(infoMessage, capturedInfoMessage);
        verify(sender, times(1)).send(simpleMailMessageArgumentCaptor.capture());
        SimpleMailMessage capturedSimpleMailMessage = simpleMailMessageArgumentCaptor.getValue();
        assertEquals(message, capturedSimpleMailMessage);
    }
}