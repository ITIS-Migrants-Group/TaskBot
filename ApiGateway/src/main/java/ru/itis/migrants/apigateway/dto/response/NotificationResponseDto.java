package ru.itis.migrants.apigateway.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.itis.migrants.apigateway.dto.enums.NotificationType;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public record NotificationResponseDto(
        UUID id,
        long ownerId,
        String title,
        UUID taskId,
        NotificationType type,
        OffsetDateTime notifyAt,
        String period,
        @JsonProperty("is_active")
        boolean isActive
) {
}
