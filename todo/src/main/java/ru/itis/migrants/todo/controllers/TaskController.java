package ru.itis.migrants.todo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.migrants.todo.controller.api.TasksApi;
import ru.itis.migrants.todo.dto.GetTasksRequest;
import ru.itis.migrants.todo.dto.TaskDto;

import ru.itis.migrants.todo.dto.TaskUpdateDto;
import ru.itis.migrants.todo.model.TaskResponseDto;
import ru.itis.migrants.todo.services.TaskService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController implements TasksApi {

    private final TaskService taskService;

    @PostMapping("/{tg-id}")
    public ResponseEntity<TaskResponseDto> createTask(
            @PathVariable("tg-id") Long tgId,
            @RequestBody TaskDto taskDto
    ) {
        log.debug("Создание задачи: {}, {}, {}", tgId, taskDto.getTitle(), taskDto.getDeadline());
        TaskResponseDto responseDto = taskService.create(tgId, TaskDto.builder()
                .title(taskDto.getTitle())
                .deadline(taskDto.getDeadline())
                .build());
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/{tg-id}")
    public ResponseEntity<List<TaskResponseDto>> getFilteredTasks(
            @PathVariable("tg-id") Long tgChatId,
            @RequestBody GetTasksRequest request
    ) {
        log.debug("Получение задач по фильтрам: {}", request);
        return ResponseEntity.ok(
                taskService.getTasks(tgChatId, request.status(), request.endedAt())
        );
    }

    @GetMapping("/{tg-id}/search")
    public ResponseEntity<List<TaskResponseDto>> getByKeyWords(
            @PathVariable("tg-id") Long tgId,
            @RequestParam("query") String query
    ) {
        log.debug("Запрос: {}", query);
        return ResponseEntity.ok(taskService.getAllByKeyWords(tgId, query));
    }

    @PatchMapping("/{task-id}")
    public ResponseEntity<TaskResponseDto> updateStatus(
            @PathVariable("task-id") UUID taskId,
            @RequestBody TaskUpdateDto taskUpdateDto) {
        return ResponseEntity.ok(taskService.updateStatus(taskId, taskUpdateDto));
    }


    @DeleteMapping("/{task-id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable("task-id") UUID taskId) {
        taskService.deleteByTaskId(taskId);
        return ResponseEntity.ok().build();
    }
}
