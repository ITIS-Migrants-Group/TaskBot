package ru.itis.migrants.todo.dto;

import java.time.OffsetDateTime;

public record GetTasksRequest(
        String status,
        OffsetDateTime endedAt
) {
}

