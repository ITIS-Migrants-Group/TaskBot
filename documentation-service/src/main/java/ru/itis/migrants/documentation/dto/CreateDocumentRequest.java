package ru.itis.migrants.documentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDocumentRequest(
        @NotBlank(message = "Content must not be blank")
        @Size(max = 20000, message = "Content must not exceed 20000 characters")
        String content
) {
}
