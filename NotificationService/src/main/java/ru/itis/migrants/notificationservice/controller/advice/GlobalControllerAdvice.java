package ru.itis.migrants.notificationservice.controller.advice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.itis.migrants.notificationservice.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(exception = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse>  handle(EntityNotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, e);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, Exception e) {
        return ResponseEntity.status(status.value())
                .body(new ErrorResponse(status.value(), status.getReasonPhrase(), e.getMessage()));
    }
}
