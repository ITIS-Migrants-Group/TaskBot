package ru.itis.migrants.bot.dto.request;

import java.time.OffsetDateTime;

public record GetTasksRequest(
        String status,
        OffsetDateTime endedAt
) {
}
