package ru.itis.migrants.bot.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.itis.migrants.bot.dto.NotificationResponse;
import ru.itis.migrants.bot.sender.NotificationSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitConsumer {
    private final NotificationSender sender;

    @RabbitListener(queues = "notification.push.queue")
    public void receiveNotifications(NotificationResponse response) {
        if (response == null) {
            log.error("ERROR: message don't received");
            return;
        }
        sender.send(response);
    }

}
