package ru.itis.migrants.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.itis.migrants.notificationservice.dto.NotificationResponse;
import ru.itis.migrants.notificationservice.property.RabbitProperty;
import ru.itis.migrants.notificationservice.repository.NotificationJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendNotificationService {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperty rabbitProperty;
    private final NotificationJpaRepository repository;

    public void sendNotifications(List<NotificationResponse> notifications) {
        log.debug("sender send {} notifications", notifications.size());
        try {
            for (NotificationResponse notification : notifications) {
                //Todo: добавить обработку обновления статуса нотификации

                rabbitTemplate.convertAndSend(
                        rabbitProperty.getExchange(),
                        rabbitProperty.getPushKey(),
                        notification,
                        message -> {
                            message.getMessageProperties().setMessageId(notification.id().toString());
                            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return message;
                        }
                );
                log.debug("message {} sended", notification.id());
            }
        } catch (Exception e) {
            log.warn("Message don't accepted", e);
        }
    }
}
