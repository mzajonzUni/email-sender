package pl.zajonz.emailsender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${info-queue}")
    private String infoQueueName;

    @Override
    public void sendInfo(String info) {
        rabbitTemplate.convertAndSend(infoQueueName, info);
    }

}
