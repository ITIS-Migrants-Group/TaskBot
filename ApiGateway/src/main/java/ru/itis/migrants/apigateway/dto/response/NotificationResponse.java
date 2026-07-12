package ru.itis.migrants.apigateway.dto.response;

import ru.itis.migrants.apigateway.dto.enums.NotificationType;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        long ownerId,
        String title,
        Optional<UUID> taskId,
        NotificationType type,
        OffsetDateTime notifyAt,
        Optional<Duration> period,
        boolean isActive
) {
}