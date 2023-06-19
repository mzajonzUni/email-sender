package pl.zajonz.emailsender.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.zajonz.emailsender.service.MessageSender;

@Service
@RequiredArgsConstructor
public class InfoEventListener {

    private final MessageSender messageSender;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInfoEvent(InfoEvent event) {
        messageSender.sendInfo(event.getInfoMessage());
    }

}
