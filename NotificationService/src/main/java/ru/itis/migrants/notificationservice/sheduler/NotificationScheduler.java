package ru.itis.migrants.notificationservice.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.migrants.notificationservice.dto.NotificationResponse;
import ru.itis.migrants.notificationservice.service.NotificationService;
import ru.itis.migrants.notificationservice.service.SendNotificationService;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationService notificationService;
    private final SendNotificationService sendService;

    @Scheduled(fixedDelay = 30000)
    public void schedule() {
        OffsetDateTime now = OffsetDateTime.now();
        log.debug("Работа планировщика");
        List<NotificationResponse> response = notificationService.getForScheduler(now);
        sendService.sendNotifications(response);
    }
}
