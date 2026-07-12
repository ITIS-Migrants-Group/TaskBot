package ru.itis.migrants.contactservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import ru.itis.migrants.contactservice.annotation.PhoneNumber;

import java.util.UUID;

public record UpdateContactRequest
(
        UUID id,
        @NotBlank String name,
        @PhoneNumber String phoneNumber,
        @Email String email,
        String company,
        String note
) {}
