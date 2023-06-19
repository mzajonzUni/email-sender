package pl.zajonz.emailsender.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import pl.zajonz.emailsender.model.InfoMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ListenerUnitTest {

    @InjectMocks
    private Listener listener;
    @Mock
    private EmailService emailService;
    @Captor
    private ArgumentCaptor<InfoMessage> infoMessageArgumentCaptor;

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

        //when
        listener.listen(infoMessage);

        //then
        verify(emailService, times(1)).send(infoMessageArgumentCaptor.capture());
        InfoMessage capturedInfoMessage = infoMessageArgumentCaptor.getValue();
        assertEquals(infoMessage, capturedInfoMessage);
    }
}