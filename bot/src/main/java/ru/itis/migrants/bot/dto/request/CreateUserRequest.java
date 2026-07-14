package ru.itis.migrants.bot.dto.request;

public record CreateUserRequest(
        Long tgId,
        String username
) {
}