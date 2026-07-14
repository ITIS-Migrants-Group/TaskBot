package ru.itis.migrants.todo.services;

import ru.itis.migrants.todo.dto.TaskDto;
import ru.itis.migrants.todo.dto.TaskFilterRequestDto;
import ru.itis.migrants.todo.dto.TaskUpdateDto;
import ru.itis.migrants.todo.model.TaskResponseDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskResponseDto create(Long tgChat, TaskDto taskDto);

    List<TaskResponseDto> getTasks(Long tgChatId, String status, OffsetDateTime deadline);

    List<TaskResponseDto> getAllByKeyWords(Long tgChatId, String query);

    TaskResponseDto updateStatus(UUID taskId, TaskUpdateDto dto);

    void deleteByTaskId(UUID taskId);
}
