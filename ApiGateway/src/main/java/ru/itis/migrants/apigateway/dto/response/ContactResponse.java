package ru.itis.migrants.apigateway.dto.response;

import java.util.UUID;

public record ContactResponse(
        UUID id,
        Long ownerId,
        String name,
        String phoneNumber,
        String email,
        String company,
        String note
) {
}
