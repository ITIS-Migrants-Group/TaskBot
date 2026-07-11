package ru.itis.migrants.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
    @JsonProperty("tg-id")
    Long tgId,
    String username
) {
}
