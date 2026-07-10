package ru.itis.migrants.notificationservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.migrants.notificationservice.dto.CreateNotificationRequest;
import ru.itis.migrants.notificationservice.dto.NotificationResponse;
import ru.itis.migrants.notificationservice.dto.UpdateNotificationRequest;
import ru.itis.migrants.notificationservice.mapper.NotificationMapper;
import ru.itis.migrants.notificationservice.model.Notification;
import ru.itis.migrants.notificationservice.repository.NotificationJpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationMapper mapper;

    @Transactional
    public NotificationResponse createNotification(Long tgId, CreateNotificationRequest request) {
        log.info(String.valueOf(request));
        Notification notification = Notification.builder()
                .title(request.title())
                .taskId(request.taskId().orElse(null))
                .ownerId(tgId)
                .type(request.type())
                .notifyAt(request.notifyAt())
                .period(request.period().orElse(null))
                .isActive(Boolean.TRUE)
                .build();

        Notification saved = notificationJpaRepository.save(notification);
        log.debug("New notification created: {}", notification);
        return mapper.fromEntity(saved);
    }

    @Transactional
    public NotificationResponse updateNotification(UpdateNotificationRequest request) throws EntityNotFoundException {
        Notification entity = notificationJpaRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        entity.setIsActive(request.isActive());
        Notification saved = notificationJpaRepository.save(entity);
        return mapper.fromEntity(saved);
    }

    public List<NotificationResponse> getForScheduler(OffsetDateTime time) {
        List<Notification> notifications = notificationJpaRepository.findForScheduler(time);
        log.debug("To {} get {} notification(s)", time, notifications.size());

        return notifications.stream().map(mapper::fromEntity).toList();
    }

}
