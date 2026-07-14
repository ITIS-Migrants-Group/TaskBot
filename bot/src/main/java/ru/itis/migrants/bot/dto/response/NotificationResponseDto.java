package ru.itis.migrants.bot.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.itis.migrants.bot.dto.enums.NotificationType;


import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificationResponseDto(
        UUID id,
        long ownerId,
        String title,
        UUID taskId,
        NotificationType type,
        @JsonProperty("notify_at")
        OffsetDateTime notifyAt,
        String period,
        @JsonProperty("is_active")
        boolean isActive
) {
}
