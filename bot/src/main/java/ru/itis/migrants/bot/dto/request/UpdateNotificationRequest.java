package ru.itis.migrants.bot.dto.request;

public record UpdateNotificationRequest(
        Long id,
        String title,
        String message,
        String status
) {
}