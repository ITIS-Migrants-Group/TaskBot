package ru.itis.migrants.notificationservice.dto;

public record UpdateNotificationRequest
(
        Long id,
        String title,
        String message,
        String status
) {}
