package ru.itis.migrants.apigateway.dto.request;

public record CreateUserRequest(
        Long tgId,
        String username
) {
}