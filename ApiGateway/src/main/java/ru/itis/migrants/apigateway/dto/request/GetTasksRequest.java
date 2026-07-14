package ru.itis.migrants.apigateway.dto.request;

import java.time.OffsetDateTime;

public record GetTasksRequest(
        String status,
        OffsetDateTime endedAt
) {
}
