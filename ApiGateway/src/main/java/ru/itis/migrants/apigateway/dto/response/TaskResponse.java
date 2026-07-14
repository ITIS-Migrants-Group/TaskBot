package ru.itis.migrants.apigateway.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.itis.migrants.apigateway.dto.enums.TaskStatus;

import java.time.OffsetDateTime;

public record TaskResponse(
        String id,

        Long userId,

        String title,

        OffsetDateTime createdAt,

        OffsetDateTime endedAt,

        String status
) {
}