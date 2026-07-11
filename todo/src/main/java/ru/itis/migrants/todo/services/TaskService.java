package ru.itis.migrants.todo.services;

import ru.itis.migrants.todo.dto.TaskDto;
import ru.itis.migrants.todo.dto.TaskFilterRequestDto;
import ru.itis.migrants.todo.dto.TaskUpdateDto;
import ru.itis.migrants.todo.model.TaskResponseDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskResponseDto create(TaskDto taskDto);

    List<TaskResponseDto> getAllByStatusAndDeadline(TaskFilterRequestDto taskFilterRequestDto);

    List<TaskResponseDto> getAllByKeyWords(Long tgChatId, String query);

    TaskResponseDto updateStatus(UUID taskId, TaskUpdateDto dto);

    void deleteByTaskId(UUID taskId);
}
