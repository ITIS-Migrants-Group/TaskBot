package ru.itis.migrants.todo.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itis.migrants.todo.exceptions.TaskNotFoundException;
import ru.itis.migrants.todo.models.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .description("Не найдена задача")
                .exceptionName(ex.getClass().getSimpleName())
                .exceptionMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(response);
    }
}
