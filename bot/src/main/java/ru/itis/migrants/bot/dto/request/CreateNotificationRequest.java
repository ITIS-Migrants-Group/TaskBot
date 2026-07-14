package ru.itis.migrants.bot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Builder;
import ru.itis.migrants.bot.dto.enums.NotificationType;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record CreateNotificationRequest
        (
                String title,
                @Nullable UUID taskId,
                NotificationType type,
                OffsetDateTime notifyAt,
                @Nullable Duration period
        ) {}