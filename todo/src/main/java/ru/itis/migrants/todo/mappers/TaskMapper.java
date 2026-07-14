package ru.itis.migrants.todo.mappers;

import org.springframework.stereotype.Component;
import ru.itis.migrants.todo.dto.TaskDto;
import ru.itis.migrants.todo.model.TaskResponseDto;
import ru.itis.migrants.todo.models.Task;
import ru.itis.migrants.todo.models.TaskStatus;

import java.time.OffsetDateTime;

@Component
public class TaskMapper {

    public Task toEntity(Long tgChatId, TaskDto dto) {
        return Task.builder()
                .tgChatId(tgChatId)
                .title(dto.getTitle())
                .createdAt(OffsetDateTime.now())
                .endedAt(dto.getDeadline())
                .status(TaskStatus.NEW)
                .build();
    }

    public TaskResponseDto toResponseDto(Task task) {
        return new TaskResponseDto()
                .id(task.getId())
                .tgChatId(task.getTgChatId())
                .title(task.getTitle())
                .createdAt(task.getCreatedAt())
                .endedAt(task.getEndedAt())
                .status(task.getStatus().toString());
    }
}
