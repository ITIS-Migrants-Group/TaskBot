package ru.itis.migrants.bot.dto.request;

public record CreateContactRequest(
        String name,
        String phoneNumber,
        String email,
        String company,
        String note
) {
}
