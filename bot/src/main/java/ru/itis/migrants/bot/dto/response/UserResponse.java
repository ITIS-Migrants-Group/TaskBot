package ru.itis.migrants.bot.dto.response;

public record UserResponse(
        Long tgId,
        String username
) {
}