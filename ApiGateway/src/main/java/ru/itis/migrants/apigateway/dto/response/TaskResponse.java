package ru.itis.migrants.apigateway.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.itis.migrants.apigateway.dto.enums.TaskStatus;

import java.time.OffsetDateTime;

public record TaskResponse(
        String id,

        @JsonProperty("userId")
        Long userId,

        String title,

        @JsonProperty("at_created")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime createdAt,

        @JsonProperty("at_ended")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        OffsetDateTime endedAt,

        TaskStatus status
) {
}