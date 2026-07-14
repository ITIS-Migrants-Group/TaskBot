package ru.itis.migrants.notificationservice.dto;

import java.util.UUID;

public record UpdateNotificationRequest
(
        UUID id,
        String title,
        String message,
        String status
) {}
