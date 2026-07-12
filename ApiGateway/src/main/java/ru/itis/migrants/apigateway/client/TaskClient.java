package ru.itis.migrants.apigateway.client;

import org.springframework.stereotype.Component;
import org.springframework.web.service.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import ru.itis.migrants.apigateway.dto.request.UpdateTaskRequest;
import ru.itis.migrants.apigateway.dto.response.TaskResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@HttpExchange("/tasks")
public interface TaskClient {

    @PostExchange("/{tg-id}")
    ResponseEntity<TaskResponse> create(
            @PathVariable("tg-id") Long tgId,
            String title,
            OffsetDateTime deadline
    );

    /**
     * Получение задачи по ID
     */
    @GetExchange("/{task-id}")
    ResponseEntity<TaskResponse> get(
            @PathVariable("task-id") Long taskId
    );

    /**
     * Получение всех задач пользователя
     */
    @GetExchange("/{tg-id}")
    ResponseEntity<List<TaskResponse>> getAll(
            @PathVariable("tg-id") Long tgId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) OffsetDateTime at_ended
    );

    /**
     * Обновление задачи
     */
    @PutExchange("/{task-id}")
    ResponseEntity<TaskResponse> update(
            @PathVariable("task-id") Long taskId,
            @RequestBody UpdateTaskRequest request
    );

    /**
     * Удаление задачи
     */
    @DeleteExchange("/{task-id}/{id}")
    ResponseEntity<Void> deleteTask(
            @PathVariable("task-id") Long taskId,
            @PathVariable Long id
    );

    @GetExchange("/search")
    ResponseEntity<List<TaskResponse>> search(@RequestParam String query);
}
