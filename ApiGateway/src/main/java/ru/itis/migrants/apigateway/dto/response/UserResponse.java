package ru.itis.migrants.apigateway.dto.response;

public record UserResponse(
        Long tgId,
        String username
) {
}