package ru.itis.migrants.notificationservice.dto;

import ru.itis.migrants.notificationservice.model.NotificationType;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public record CreateNotificationRequest
(
        String title,
        UUID taskId,
        NotificationType type,
        OffsetDateTime notifyAt,
        Duration period
) {}
