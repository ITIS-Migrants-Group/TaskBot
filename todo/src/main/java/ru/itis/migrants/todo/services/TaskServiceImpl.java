package ru.itis.migrants.todo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.migrants.todo.dto.TaskDto;
import ru.itis.migrants.todo.dto.TaskFilterRequestDto;
import ru.itis.migrants.todo.dto.TaskUpdateDto;
import ru.itis.migrants.todo.exceptions.TaskNotFoundException;
import ru.itis.migrants.todo.mappers.TaskMapper;
import ru.itis.migrants.todo.model.TaskResponseDto;
import ru.itis.migrants.todo.models.Task;
import ru.itis.migrants.todo.models.TaskStatus;
import ru.itis.migrants.todo.repositories.TaskRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper mapper;

    @Override
    public TaskResponseDto create(TaskDto taskDto) {
        Task task = taskRepository.save(mapper.toEntity(taskDto));

        return mapper.toResponseDto(task);
    }

    @Override
    public List<TaskResponseDto> getAllByStatusAndDeadline(TaskFilterRequestDto taskFilterRequestDto) {
        List<Task> tasks = taskRepository.findAllByTgChatIdAndStatusAndEndedAt(
                taskFilterRequestDto.tgChatId(),
                TaskStatus.valueOf(taskFilterRequestDto.status()),
                taskFilterRequestDto.endedAt());

        return tasks.stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getAllByKeyWords(Long tgChatId, String query) {
        List<Task> tasks = taskRepository.findByKeyword(tgChatId, query);

        return tasks.stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    public TaskResponseDto updateStatus(UUID taskId, TaskUpdateDto dto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача с таким Id=" + taskId + " не найдена"));
        task.setStatus(TaskStatus.valueOf(dto.getStatus()));

        return mapper.toResponseDto(taskRepository.save(task));
    }

    @Override
    public void deleteByTaskId(UUID taskId) {
        taskRepository.deleteById(taskId);
    }
}
