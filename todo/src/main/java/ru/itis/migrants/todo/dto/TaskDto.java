package ru.itis.migrants.todo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    @NotNull
    private Long tgChatId;

    @NotNull
    private String title;

    @NotNull
    private OffsetDateTime deadline;
}
