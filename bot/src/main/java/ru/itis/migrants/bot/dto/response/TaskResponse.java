package ru.itis.migrants.bot.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record TaskResponse(
        String id,

        @JsonProperty("userId")
        Long userId,

        String title,

        OffsetDateTime createdAt,

        OffsetDateTime endedAt,

        String status
) {
}