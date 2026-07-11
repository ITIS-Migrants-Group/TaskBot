package ru.itis.migrants.notificationservice.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.notificationservice.dto.NotificationResponse;
import ru.itis.migrants.notificationservice.handler.SaveSolver;
import ru.itis.migrants.notificationservice.mapper.NotificationMapper;
import ru.itis.migrants.notificationservice.model.Notification;
import ru.itis.migrants.notificationservice.model.NotificationType;
import ru.itis.migrants.notificationservice.repository.NotificationJpaRepository;

import java.time.OffsetDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class PeriodSaveSolver implements SaveSolver {
    private final NotificationJpaRepository repository;
    private final NotificationMapper mapper;

    @Override
    public boolean solve(NotificationResponse response, OffsetDateTime offsetDateTime) {
        try {
            if (offsetDateTime.isAfter(response.notifyAt())) {
                Notification entity = mapper.toEntity(response);

                OffsetDateTime newNotifyAt = entity.getNotifyAt().plus(entity.getPeriod());
                entity.setNotifyAt(newNotifyAt);
                repository.save(entity);
                return true;
            }
        } catch (Exception e) {
            log.debug("Response don't solved: {}", response);
            return false;
        }
        return false;
    }

    @Override
    public boolean support(NotificationResponse response) {
        return response.isActive() && response.period().isPresent() && response.type().equals(NotificationType.PERIOD);
    }
}
