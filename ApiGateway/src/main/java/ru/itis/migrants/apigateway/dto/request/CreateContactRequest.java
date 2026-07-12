package ru.itis.migrants.apigateway.dto.request;

public record CreateContactRequest(
        String name,
        String phoneNumber,
        String email,
        String company,
        String note
) {
}
