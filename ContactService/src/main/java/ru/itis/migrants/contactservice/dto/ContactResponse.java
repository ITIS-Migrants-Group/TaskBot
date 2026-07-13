package ru.itis.migrants.contactservice.dto;
import java.util.UUID;

public record ContactResponse
(
        UUID id,
        Long ownerId,
        String name,
        String phoneNumber,
        String email,
        String company,
        String note
) {}
