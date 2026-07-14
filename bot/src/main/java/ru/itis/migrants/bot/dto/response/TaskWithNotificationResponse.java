package ru.itis.migrants.bot.dto.response;

// DTO для объединённого ответа
public record TaskWithNotificationResponse(
        TaskResponse task,
        NotificationResponse notification
) {
}