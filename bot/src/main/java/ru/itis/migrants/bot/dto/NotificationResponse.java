package ru.itis.migrants.bot.dto;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public record NotificationResponse
        (
                UUID id,
                long ownerId,
                String title,
                UUID taskId,
                NotificationType type,
                OffsetDateTime notifyAt,
                Duration period,
                boolean isActive
        ) {}
