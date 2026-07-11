package ru.itis.migrants.notificationservice.dto;

import java.util.UUID;

public record UpdateNotificationRequest
(
        UUID id,
        boolean isActive
) {}
