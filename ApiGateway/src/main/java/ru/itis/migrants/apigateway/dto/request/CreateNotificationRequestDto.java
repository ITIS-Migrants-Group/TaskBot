package ru.itis.migrants.apigateway.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.itis.migrants.apigateway.dto.enums.NotificationType;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public record CreateNotificationRequestDto(
        String title,
        Optional<UUID> taskId,
        NotificationType type,
        OffsetDateTime notifyAt,
        Optional<Duration> period
) {
}
