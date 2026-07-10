package ru.itis.migrants.todo.dto;

import java.time.OffsetDateTime;

public record TaskFilterRequestDto(
        Long tgChatId,
        String status,
        OffsetDateTime endedAt
) {
}
