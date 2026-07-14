package ru.itis.migrants.bot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record CreateTaskRequest(
        String title,
        OffsetDateTime deadline,
        String notifyFor
) {
}