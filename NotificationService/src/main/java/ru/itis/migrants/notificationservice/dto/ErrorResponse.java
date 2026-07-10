package ru.itis.migrants.notificationservice.dto;

public record ErrorResponse
(
        int status,
        String code,
        String message
) {}
