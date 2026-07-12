package ru.itis.migrants.contactservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import ru.itis.migrants.contactservice.annotation.PhoneNumber;

public record CreateContactRequest
(
        @NotBlank String name,
        @PhoneNumber String phoneNumber,
        @Email String email,
        String company,
        String note
) {}
