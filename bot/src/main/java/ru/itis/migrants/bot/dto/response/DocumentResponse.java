package ru.itis.migrants.bot.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        Long ownerId,
        String content,
        @JsonProperty("created_at")
        OffsetDateTime createdAt
) {
}
