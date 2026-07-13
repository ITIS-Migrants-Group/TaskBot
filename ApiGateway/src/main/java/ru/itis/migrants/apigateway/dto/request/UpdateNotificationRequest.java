package ru.itis.migrants.apigateway.dto.request;

public record UpdateNotificationRequest(
        Long id,
        String title,
        String message,
        String status
) {
}