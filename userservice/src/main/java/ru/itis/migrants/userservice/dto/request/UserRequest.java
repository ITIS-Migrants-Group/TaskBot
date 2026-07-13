package ru.itis.migrants.userservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель для создания User")
public record UserRequest(

        @Schema(description = "Уникальный идентификатор пользователя в Telegram")
        Long tgId,

        @Schema(description = "Имя пользователя")
        String username
) {
}
