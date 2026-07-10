package ru.itis.migrants.documentation.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        Long ownerId,
        String content,
        OffsetDateTime createdAt
) {
}
