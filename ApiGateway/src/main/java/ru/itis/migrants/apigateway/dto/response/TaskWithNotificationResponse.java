package ru.itis.migrants.apigateway.dto.response;

// DTO для объединённого ответа
public record TaskWithNotificationResponse(
        TaskResponse task,
        NotificationResponse notification
) {
}