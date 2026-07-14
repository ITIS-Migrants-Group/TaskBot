package ru.itis.migrants.apigateway.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        Long ownerId,
        String content,
        OffsetDateTime createdAt
) {
}
