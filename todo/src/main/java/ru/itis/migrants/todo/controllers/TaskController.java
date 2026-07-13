package ru.itis.migrants.todo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.migrants.todo.controller.api.TasksApi;
import ru.itis.migrants.todo.dto.TaskDto;
import ru.itis.migrants.todo.dto.TaskFilterRequestDto;
import ru.itis.migrants.todo.dto.TaskUpdateDto;
import ru.itis.migrants.todo.model.TaskResponseDto;
import ru.itis.migrants.todo.services.TaskService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController implements TasksApi {

    private final TaskService taskService;

    @PostMapping("/{tg-id}")
    public ResponseEntity<TaskResponseDto> createTask(
            @PathVariable("tg-id") Long tgId,
            String title,
            OffsetDateTime deadline
    ) {
        TaskResponseDto responseDto = taskService.create(TaskDto.builder()
                .tgChatId(tgId)
                .title(title)
                .deadline(deadline)
                .build());
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/{tg-id}")
    public ResponseEntity<List<TaskResponseDto>> getFilteredTasks(
            @PathVariable("tg-id") Long tgChatId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "ended_at", required = false) OffsetDateTime deadline
    ) {
        return ResponseEntity.ok(
                taskService.getTasks(tgChatId, status, deadline)
        );
    }

    @GetMapping("/{tg-id}/search")
    public ResponseEntity<List<TaskResponseDto>> getByKeyWords(
            @PathVariable("tg-id") Long tgChatId,
            @RequestParam("query") String query
    ) {
        return ResponseEntity.ok(taskService.getAllByKeyWords(tgChatId, query));
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
