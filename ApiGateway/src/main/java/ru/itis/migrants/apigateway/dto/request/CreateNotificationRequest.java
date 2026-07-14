package ru.itis.migrants.apigateway.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import ru.itis.migrants.apigateway.dto.enums.NotificationType;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public record CreateNotificationRequest
        (
                String title,
                @Nullable UUID taskId,
                NotificationType type,
                OffsetDateTime notifyAt,
                @Nullable Duration period
        ) {}